package com.mballester.minesweeper;

import com.mballester.minesweeper.exceptions.GameStillActiveException;
import com.mballester.minesweeper.model.Cell;
import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.GameBoard;
import com.mballester.minesweeper.model.GameBoardActionSettings;
import com.mballester.minesweeper.model.GameBoardSettings;
import com.mballester.minesweeper.model.States;
import com.mballester.minesweeper.repository.GameRepository;
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
    MinesWeeperServiceImpl minesWeeperService;

    @MockBean
    private GameRepository gameRepository;

    Game game;
    String userName;
    Optional<List<Game>> listOptional;

    @Before
    public void init() {
        Cell[][] board = createCells(2, 2);
        userName = "Matias";
        game = new Game(board, userName);

        List<Game> userGames = new ArrayList<>(1);
        userGames.add(game);

        listOptional = Optional.of(userGames);
    }

    @org.junit.Test
    public void testStartGameSuccessfully() {
        listOptional = Optional.of(new ArrayList<>());
        when(gameRepository.save(game)).thenReturn(game);
        when(gameRepository.findByUserNameAndState(userName, States.ACTIVE)).thenReturn(listOptional);

        Game newGame = minesWeeperService.createGame(new GameBoardSettings(userName, 2, 2, 3 ));

        assertThat(newGame.getUserName()).isEqualTo(userName);
        assertThat(newGame.getState()).isEqualByComparingTo(States.ACTIVE);
        assertThat(newGame.getBoard().length).isEqualTo(2);
        assertThat(newGame.getBoard()[0].length).isEqualTo(2);
    }

    @org.junit.Test(expected = GameStillActiveException.class)
    public void testGameStillActive() {
        when(gameRepository.findByUserNameAndState(userName, States.ACTIVE)).thenReturn(listOptional);
        minesWeeperService.createGame(new GameBoardSettings(userName, 2, 2, 3 ));
    }

    @org.junit.Test
    public void testUsersLoss() {
        GameBoard gameBoard = new GameBoard(2, 2, 3, game.getBoard());
        gameBoard.loadMines();
        gameBoard.loadNearMines();

        List<Game> userGames = new ArrayList<>(1);
        userGames.add(game);
        when(gameRepository.findByUserNameAndState(userName, States.ACTIVE)).thenReturn(Optional.of(userGames));

        Cell minedCell = getMinedCell(game.getBoard());
        Game returnedGame = minesWeeperService.playGame(new GameBoardActionSettings(userName, minedCell.getRow(), minedCell.getCol()));

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
