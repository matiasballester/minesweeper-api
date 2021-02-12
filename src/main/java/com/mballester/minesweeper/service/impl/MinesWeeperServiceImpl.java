package com.mballester.minesweeper.service.impl;

import com.mballester.minesweeper.exceptions.GameNotActiveException;
import com.mballester.minesweeper.exceptions.GameNotFoundException;
import com.mballester.minesweeper.exceptions.GameStillActiveException;
import com.mballester.minesweeper.model.*;
import com.mballester.minesweeper.repository.GameRepository;
import com.mballester.minesweeper.service.MinesWeeperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class MinesWeeperServiceImpl implements MinesWeeperService {

    private Logger logger = LoggerFactory.getLogger(MinesWeeperServiceImpl.class);

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Game createGame(GameBoardSettings gameBoardSettings) {
        if ("".equals(gameBoardSettings.getUserName())) throw new IllegalArgumentException("User name cannot be empty");
        if (checkGameActive(gameBoardSettings.getUserName())) throw new GameStillActiveException("Game for user " + gameBoardSettings.getUserName() + " is still active");
        if (gameBoardSettings.getRows() <= 0) throw new IllegalArgumentException("Invalid number of rows");
        if (gameBoardSettings.getCols() <= 0) throw new IllegalArgumentException("Invalid number of columns");
        if (gameBoardSettings.getMines() < 1) throw new IllegalArgumentException("Invalid number of mines");
        if (gameBoardSettings.getMines() >= gameBoardSettings.getCols() * gameBoardSettings.getRows()) throw new IllegalArgumentException("Please use less number of mines");

        Cell[][] board = createCells(gameBoardSettings.getRows(), gameBoardSettings.getCols());
        logger.debug("Board created with dimension - Rows: " + board.length + ", Columns: " + board[0].length);

        GameBoard gameBoard = new GameBoard(gameBoardSettings.getRows(), gameBoardSettings.getCols(), gameBoardSettings.getMines(), board);
        gameBoard.loadMines();
        gameBoard.loadNearMines();

        Game game = new Game(board, gameBoardSettings.getUserName());
        gameRepository.save(game);
        logger.debug("Game ID " + game.getId() + " for user: " + game.getUserName() + " successfully created");

        return game;
    }

    @Override
    public Game playGame(GameBoardActionSettings gameBoardActionSettings) {
        Game game = loadActiveGameByUser(gameBoardActionSettings.getUserName());
        GameBoardAction gameBoardAction = new GameBoardAction(gameBoardActionSettings.getRow(), gameBoardActionSettings.getColumn(), game.getBoard());

        logger.debug("User " + gameBoardActionSettings.getUserName() + " selects cell [" + gameBoardActionSettings.getRow() + "][" + gameBoardActionSettings.getColumn() + "]");

        if(! gameBoardAction.isFlagged()) {
            if (gameBoardAction.isMined()) {
                logger.debug("User " + gameBoardActionSettings.getUserName() + " selected a mined cell. Game ended");
                game.setEndTime(LocalDateTime.now());
                game.setState(States.LOST);
                gameBoardAction.revealMines();
            } else {
                gameBoardAction.reveal();
                gameBoardAction.revealNeighbors();
                if (gameBoardAction.checkWin()) {
                    logger.debug("User " + gameBoardActionSettings.getUserName() + " win");
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
    public Game flagCell(GameBoardActionSettings gameBoardActionSettings) {
        Game game = loadActiveGameByUser(gameBoardActionSettings.getUserName());
        GameBoardAction gameBoardAction = new GameBoardAction(gameBoardActionSettings.getRow(), gameBoardActionSettings.getColumn(), game.getBoard());
        gameBoardAction.flagCell();
        gameRepository.save(game);
        return game;
    }

    @Override
    public List<Game> getGamesByUserAndStatus(String userName, States state) {
        if(state == null)
            return gameRepository.findByUserName(userName).orElse(Collections.EMPTY_LIST);
        else
            return gameRepository.findByUserNameAndState(userName, state).orElse(Collections.EMPTY_LIST);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGame(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Game ID : " + id + " cannot be found"));
    }

    private Game loadActiveGameByUser(String userName){
        Optional<List<Game>> optionalGame = gameRepository.findByUserNameAndState(userName, States.ACTIVE);
        if(optionalGame.get().size() == 0) {
            throw new GameNotActiveException("Game not active for user " + userName);
        }
        return optionalGame.get().get(0);
    }

    private boolean checkGameActive(String userName) {
        List<Game> games = gameRepository.findByUserNameAndState(userName, States.ACTIVE).get();
        return games.size() > 0;
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
