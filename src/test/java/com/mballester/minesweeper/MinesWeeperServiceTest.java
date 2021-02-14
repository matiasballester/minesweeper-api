package com.mballester.minesweeper;

import com.mballester.minesweeper.exceptions.UserAlreadyExistsException;
import com.mballester.minesweeper.model.Cell;
import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.GameBoard;
import com.mballester.minesweeper.model.GameBoardActionSettings;
import com.mballester.minesweeper.model.GameBoardSettings;
import com.mballester.minesweeper.model.States;
import com.mballester.minesweeper.model.User;
import com.mballester.minesweeper.model.UserRequest;
import com.mballester.minesweeper.repository.GameRepository;
import com.mballester.minesweeper.repository.UserRepository;
import com.mballester.minesweeper.service.impl.MinesWeeperServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MinesWeeperServiceImpl.class})
public class MinesWeeperServiceTest {

    @Autowired
    private MinesWeeperServiceImpl minesWeeperService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private UserRepository userRepository;

    private Game game;
    private User user;
    private Optional<List<Game>> listOptional;

    @Before
    public void init() {
        Cell[][] board = createCells(2, 2);
        user = new User("Matias", "Matias");
        game = new Game(board, user);

        List<Game> userGames = new ArrayList<>(1);
        userGames.add(game);

        listOptional = Optional.of(userGames);
    }

    @org.junit.Test
    public void testStartGameSuccessfully() {
        listOptional = Optional.of(new ArrayList<>());
        when(gameRepository.save(game)).thenReturn(game);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Game newGame = minesWeeperService.createGame(new GameBoardSettings(user.getId(), 2, 2, 3 ));

        assertThat(newGame.getUser().getUserName()).isEqualTo("Matias");
        assertThat(newGame.getState()).isEqualTo(States.ACTIVE);
        assertThat(newGame.getBoard().length).isEqualTo(2);
        assertThat(newGame.getBoard()[0].length).isEqualTo(2);
    }

    @org.junit.Test(expected = UserAlreadyExistsException.class)
    public void testUserNotFound() {
        when(userRepository.findByUserName("Matias")).thenReturn(user);
        minesWeeperService.createUser(new UserRequest("Matias", "matias"));
    }

    @org.junit.Test
    public void testLost() {
        when(gameRepository.findById(0l)).thenReturn(Optional.of(game));
        GameBoard gameBoard = new GameBoard(2, 2, 3, game.getBoard());
        gameBoard.loadMines();
        gameBoard.loadNearMines();

        List<Game> userGames = new ArrayList<>(1);
        userGames.add(game);

        Cell minedCell = getMinedCell(game.getBoard());
        Game returnedGame = minesWeeperService.playGame(new GameBoardActionSettings(game.getId(), minedCell.getRow(), minedCell.getCol()));

        assertThat(returnedGame.getState()).isEqualByComparingTo(States.LOST);
    }

    private Cell getMinedCell(Cell[][] board) {
        Cell minedCell = null;
        rowLoop:
        for (int rowIndex=0 ; rowIndex < board.length ; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[0].length; columnIndex++) {
                if (board[rowIndex][columnIndex].isMined()) {
                    minedCell = new Cell(rowIndex, rowIndex, columnIndex);
                    minedCell.setMined(true);
                    break rowLoop;
                }
            }
        }
        return minedCell;
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
