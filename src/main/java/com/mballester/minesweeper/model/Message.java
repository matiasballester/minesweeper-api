package com.mballester.minesweeper.model;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 904580124116256409L;

    private String message;

    public Message() {
        super();
    }

    public Message(String message) {
        this.message = message;
    }

}