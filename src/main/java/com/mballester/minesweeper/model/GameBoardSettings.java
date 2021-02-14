package com.mballester.minesweeper.model;

public class GameBoardSettings {
    private String userName;
    private int rows;
    private int cols;
    private int mines;
    private long userId;

    public GameBoardSettings() {}

    public GameBoardSettings(String userName, int rows, int cols, int mines) {
        this.userName = userName;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
    }

    public GameBoardSettings(Long userId, int rows, int cols, int mines) {
        this.userId = userId;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
