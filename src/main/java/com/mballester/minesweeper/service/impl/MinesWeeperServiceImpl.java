package com.mballester.minesweeper.service.impl;

import com.mballester.minesweeper.exceptions.GameNotFoundException;
import com.mballester.minesweeper.exceptions.UserAlreadyExistsException;
import com.mballester.minesweeper.exceptions.UserNotFoundException;
import com.mballester.minesweeper.model.*;
import com.mballester.minesweeper.repository.GameRepository;
import com.mballester.minesweeper.repository.UserRepository;
import com.mballester.minesweeper.service.MinesWeeperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MinesWeeperServiceImpl implements MinesWeeperService {

    private Logger logger = LoggerFactory.getLogger(MinesWeeperServiceImpl.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Game createGame(GameBoardInput gameBoardInput) {
        User user = userRepository.findById(gameBoardInput.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user"));
        if (gameBoardInput.getMines() >= gameBoardInput.getCols() * gameBoardInput.getRows()) throw new IllegalArgumentException("Please use less number of mines");

        Cell[][] board = createCells(gameBoardInput.getRows(), gameBoardInput.getCols());
        logger.debug("Board created with dimension - Rows: " + board.length + ", Columns: " + board[0].length);

        GameBoard gameBoard = new GameBoard(gameBoardInput.getRows(), gameBoardInput.getCols(), gameBoardInput.getMines(), board);
        gameBoard.loadMines();
        gameBoard.loadNearMines();

        Game game = new Game(board, user);
        game.setRows(gameBoardInput.getRows());
        game.setCols(gameBoardInput.getCols());
        game.setMines(gameBoardInput.getMines());

        gameRepository.save(game);
        logger.debug("Game Id" + game.getId() + " for user " + user.getUserName() + " was successfully created");

        return game;
    }

    @Override
    public Game playGame(GameBoardActionInput gameBoardActionInput) {
        Game game = gameRepository.findById(gameBoardActionInput.getGameId()).orElseThrow(() -> new GameNotFoundException());

        GameBoardAction gameBoardAction = new GameBoardAction(gameBoardActionInput.getRow(), gameBoardActionInput.getColumn(), game.getBoard());
        logger.debug("User " + game.getUser().getUserName() + " selects cell [" + gameBoardActionInput.getRow() + "][" + gameBoardActionInput.getColumn() + "]");

        if(! gameBoardAction.isFlagged()) {
            if (gameBoardAction.isMined()) {
                logger.debug("User " + game.getUser().getUserName() + " selected a mined cell. Game ended");
                game.setEndTime(LocalDateTime.now());
                game.setState(States.LOST);
                gameBoardAction.revealMines();
            } else {
                gameBoardAction.reveal();
                gameBoardAction.revealNeighbors();
                if (gameBoardAction.checkWin()) {
                    logger.debug("User " + game.getUser().getUserName() + " win");
                    game.setEndTime(LocalDateTime.now());
                    game.setState(States.VICTORY);
                    gameBoardAction.revealMines();
                }
            }
        }
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game flagCell(GameBoardActionInput gameBoardActionInput) {
        Game game = gameRepository.findById(gameBoardActionInput.getGameId()).orElseThrow(() -> new GameNotFoundException());
        GameBoardAction gameBoardAction = new GameBoardAction(gameBoardActionInput.getRow(), gameBoardActionInput.getColumn(), game.getBoard());
        gameBoardAction.flagCell();
        gameRepository.save(game);
        return game;
    }

    @Override
    public List<Game> getGamesByUserAndStatus(Long userId, States state) {
        User user = userRepository.findById(userId).get();
        if(state == null)
            return user.getGames().stream().sorted(Comparator.comparing(Game::getStartTime).reversed()).collect(Collectors.toList());
        else
            return user.getGames().stream().filter(game -> game.getState() == state).collect(Collectors.toList());
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll(Sort.by(Sort.Direction.DESC, "endTime"));
    }

    @Override
    public Game getGame(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Game ID : " + id + " cannot be found"));
    }

    @Override
    public User createUser(UserInputRequest userInputRequest) {
        if(userRepository.findByUserName(userInputRequest.getUserName()) != null) throw new UserAlreadyExistsException();
        User user = new User(userInputRequest.getUserName(), passwordEncoder.encode(userInputRequest.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User getAuthenticatedUser(UserInputRequest userInputRequest) {
        User user = userRepository.findByUserName(userInputRequest.getUserName());
        if(user == null) throw new UserNotFoundException("User doesn't exist. Please register first");
        if(! passwordEncoder.matches(userInputRequest.getPassword(), user.getPassword())) throw new IllegalArgumentException("Username and/or password are not correct");
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
    }

    private Cell[][] createCells(int rows, int cols) {
        Cell[][] cells = new Cell[rows][cols];
        int boardIndex = 0;
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                Cell cell = new Cell(boardIndex, r, c);
                cells[r][c] = cell;
                boardIndex++;
            }
        }
        return cells;
    }
}
