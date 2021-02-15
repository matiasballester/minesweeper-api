package com.mballester.minesweeper.controller;

import com.mballester.minesweeper.model.GameBoardActionInput;
import com.mballester.minesweeper.model.GameBoardInput;
import com.mballester.minesweeper.model.GameDTO;
import com.mballester.minesweeper.model.Message;
import com.mballester.minesweeper.model.UserInputRequest;
import com.mballester.minesweeper.service.MinesWeeperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/minesweeper")
@Validated
public class MinesWeeperController {
    private Logger logger = LoggerFactory.getLogger(MinesWeeperController.class);

    @Autowired
    private MinesWeeperService minesweeperService;

    /**
     * Register a new user
     *
     * @param userInputRequest {
     *     userName, password
     * }
     * @return created user
     */
    @PostMapping("/createUser")
    public ResponseEntity createUser(@Valid @RequestBody UserInputRequest userInputRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(minesweeperService.createUser(userInputRequest));
        } catch (Exception e) {
            logger.error("Failed to create a new user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Authenticates an already existing user
     * @param userInputRequest {
     *      userName, password
     * }
     * @return DTO that contains the user id
     */
    @PostMapping("/authenticateUser")
    public ResponseEntity authenticateUser(@Valid @RequestBody UserInputRequest userInputRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(GameDTO.createGameDTO(minesweeperService.getAuthenticatedUser(userInputRequest)));
        } catch (Exception e) {
            logger.error("Failed to create a new user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Returns an user by user id
     * @param id
     * @return user
     */
    @GetMapping("/users/{id}")
    public ResponseEntity loadUser(@PathVariable @NotNull Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getUser(id));
        } catch (Exception e) {
            logger.error("Failed to load games for user id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Creates a board using a specific board configuration (#rows, #columns, #mines)
     * @param gameBoardInput {
     *    userId, rows, columns, mines
     * }
     * @return a DTO that contains the created game
     */
    @PostMapping("/startGame")
    public ResponseEntity create(@Valid @RequestBody GameBoardInput gameBoardInput) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(GameDTO.createGameDTO(minesweeperService.createGame(gameBoardInput)));
        } catch (Exception e) {
            logger.error("Failed to create a new game", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Process the game logic
     * @param gameBoardActionInput {
     *      gameId, rowIndex, columnIndex
     * }
     * @return a DTO that contains the game with the updates
     */
    @PostMapping("/playGame")
    public ResponseEntity play(@Valid @RequestBody GameBoardActionInput gameBoardActionInput) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.playGame(gameBoardActionInput)));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Adds a flag
     * @param gameBoardActionInput {
     *     gameId, rowIndex, columnIndex
     * }
     * @return a DTO that contains the game with the updates
     */
    @PostMapping("/addFlag")
    public ResponseEntity flag(@Valid @RequestBody GameBoardActionInput gameBoardActionInput) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.flagCell(gameBoardActionInput)));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Returns the user's games
     * @param id
     * @return GameDTO list
     */
    @GetMapping("/games/users/{id}")
    public ResponseEntity loadGamesByUserId(@PathVariable @NotNull Long id) {
        try {
            final List<GameDTO> gamesDTO = new ArrayList<>();
            minesweeperService.getGamesByUserAndStatus(id, null).forEach(game -> {
                gamesDTO.add(GameDTO.createGameDTO(game));
            });
            return ResponseEntity.status(HttpStatus.OK).body(gamesDTO);
        } catch (Exception e) {
            logger.error("Failed to load games for user id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Returns a game by id
     * @param id
     * @return a DTO that contains an existing the game
     */
    @GetMapping("/games/{id}")
    public ResponseEntity loadGameById(@PathVariable @NotNull Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.getGame(id)));
        } catch (Exception e) {
            logger.error("Failed to load a game by id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    /**
     * Returns all the existing games
     * @return GameDTO list
     */
    @GetMapping("/games")
    public ResponseEntity loadGames() {
        try {
            final List<GameDTO> gamesDTO = new ArrayList<>();
            minesweeperService.getAllGames().forEach(game -> {
                gamesDTO.add(GameDTO.createGameDTO(game));
            });
            return ResponseEntity.status(HttpStatus.OK).body(gamesDTO);
        } catch (Exception e) {
            logger.error("Failed to load all games");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }
}
