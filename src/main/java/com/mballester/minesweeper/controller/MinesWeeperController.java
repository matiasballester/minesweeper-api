package com.mballester.minesweeper.controller;

import com.mballester.minesweeper.model.GameBoardActionSettings;
import com.mballester.minesweeper.model.GameBoardSettings;
import com.mballester.minesweeper.model.States;
import com.mballester.minesweeper.service.MinesWeeperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/minesweeper")
public class MinesWeeperController {
    private Logger logger = LoggerFactory.getLogger(MinesWeeperController.class);

    @Autowired
    private MinesWeeperService minesweeperService;

    @PostMapping("/startGame")
    public ResponseEntity create(@RequestBody GameBoardSettings gameBoardSettings) {
        try {
            if (gameBoardSettings.getMines() >= gameBoardSettings.getCols() * gameBoardSettings.getRows()) {
                return ResponseEntity.badRequest().body("Please use less number of mines");
            }
            if("".equals(gameBoardSettings.getUserName())) {
                return ResponseEntity.badRequest().body("Please enter an username");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(minesweeperService.createGame(gameBoardSettings));
        } catch (Exception e) {
            logger.error("Failed to create a new game", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/playGame")
    public ResponseEntity play(@RequestBody GameBoardActionSettings gameBoardActionSettings) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.playGame(gameBoardActionSettings));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/addFlag")
    public ResponseEntity flag(@RequestBody GameBoardActionSettings gameBoardActionSettings) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.flagCell(gameBoardActionSettings));
        } catch (Exception e) {
            logger.error("Failed to make a move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/game")
    public ResponseEntity loadActiveGameByUserName(@RequestParam String userName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getGamesByUserAndStatus(userName, States.ACTIVE));
        } catch (Exception e) {
            logger.error("Failed to load a game by user " + userName);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/game/{id}")
    public ResponseEntity loadGameById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getGame(id));
        } catch (Exception e) {
            logger.error("Failed to load a game by id " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/games/{userName}")
    public ResponseEntity loadGamesByUserName(@PathVariable String userName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getGamesByUserAndStatus(userName, null));
        } catch (Exception e) {
            logger.error("Failed to load games by userName " + userName);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/games")
    public ResponseEntity loadGames() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minesweeperService.getAllGames());
        } catch (Exception e) {
            logger.error("Failed to load all games");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}