package com.mballester.minesweeper.service;

import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.GameBoardActionSettings;
import com.mballester.minesweeper.model.GameBoardSettings;
import com.mballester.minesweeper.model.States;
import com.mballester.minesweeper.model.User;
import com.mballester.minesweeper.model.UserRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MinesWeeperService {
    Game createGame(GameBoardSettings gameBoardSettings);

    Game playGame(GameBoardActionSettings gameBoardActionSettings);

    Game flagCell(GameBoardActionSettings gameBoardActionSettings);

    List<Game> getGamesByUserAndStatus(Long userId, States states);

    List<Game> getAllGames();

    Game getGame(Long id);

    User createUser(UserRequest userRequest);

    User getAuthenticatedUser(UserRequest userRequest);

    User getUser(Long id);
}
