package com.mballester.minesweeper.model;

public class GameBoardActionSettings {
    private String userName;
    private int row;
    private int column;

    public GameBoardActionSettings() {}

    public GameBoardActionSettings(String userName, int row, int column) {
        this.userName = userName;
        this.row = row;
        this.column = column;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
