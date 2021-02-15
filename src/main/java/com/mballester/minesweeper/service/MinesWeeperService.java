package com.mballester.minesweeper.service;

import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.GameBoardActionInput;
import com.mballester.minesweeper.model.GameBoardInput;
import com.mballester.minesweeper.model.States;
import com.mballester.minesweeper.model.User;
import com.mballester.minesweeper.model.UserInputRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MinesWeeperService {
    Game createGame(GameBoardInput gameBoardSettings);

    Game playGame(GameBoardActionInput gameBoardActionInput);

    Game flagCell(GameBoardActionInput gameBoardActionInput);

    List<Game> getGamesByUserAndStatus(Long userId, States states);

    List<Game> getAllGames();

    Game getGame(Long id);

    User createUser(UserInputRequest userInputRequest);

    User getAuthenticatedUser(UserInputRequest userInputRequest);

    User getUser(Long id);
}
