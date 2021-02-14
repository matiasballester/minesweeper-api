package com.mballester.minesweeper.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User already exists");
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
