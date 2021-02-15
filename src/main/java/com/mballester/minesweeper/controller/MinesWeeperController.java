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

    @PostMapping("/createUser")
    public ResponseEntity createUser(@Valid @RequestBody UserInputRequest userInputRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(minesweeperService.createUser(userInputRequest));
        } catch (Exception e) {
            logger.error("Failed to create a new user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @PostMapping("/startGame")
    public ResponseEntity create(@Valid @RequestBody GameBoardInput gameBoardSettings) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(GameDTO.createGameDTO(minesweeperService.createGame(gameBoardSettings)));
        } catch (Exception e) {
            logger.error("Failed to create a new game", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @PostMapping("/playGame")
    public ResponseEntity play(@Valid @RequestBody GameBoardActionInput gameBoardActionInput) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.playGame(gameBoardActionInput)));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @PostMapping("/addFlag")
    public ResponseEntity flag(@Valid @RequestBody GameBoardActionInput gameBoardActionInput) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.flagCell(gameBoardActionInput)));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @PostMapping("/authenticateUser")
    public ResponseEntity authenticateUser(@Valid @RequestBody UserInputRequest userInputRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(GameDTO.createGameDTO(minesweeperService.getAuthenticatedUser(userInputRequest)));
        } catch (Exception e) {
            logger.error("Failed to create a new user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity loadUser(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getUser(id));
        } catch (Exception e) {
            logger.error("Failed to load games for user id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

    @GetMapping("/games/user/{id}")
    public ResponseEntity loadGamesByUserId(@PathVariable Long id) {
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

    @GetMapping("/game/{id}")
    public ResponseEntity loadGameById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(GameDTO.createGameDTO(minesweeperService.getGame(id)));
        } catch (Exception e) {
            logger.error("Failed to load a game by id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(e.getMessage()));
        }
    }

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
