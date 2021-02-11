package com.mballester.minesweeper.exceptions;

public class GameNotActiveException extends RuntimeException {

    public GameNotActiveException(String message) {
        super(message);
    }

}
