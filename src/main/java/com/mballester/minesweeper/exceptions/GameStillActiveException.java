package com.mballester.minesweeper.exceptions;

public class GameStillActiveException extends RuntimeException {
    public GameStillActiveException(String message) {
        super(message);
    }
}
