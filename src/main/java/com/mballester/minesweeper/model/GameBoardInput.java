package com.mballester.minesweeper.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GameBoardInput {
    @Min(value = 1, message = "Rows cannot be less than 1")
    @Max(value = 20, message = "Rows cannot be greater than 20")
    private Integer rows;

    @Min(value = 1, message = "Columns cannot be less than 1")
    @Max(value = 20, message = "Columns cannot be greater than 20")
    private int cols;

    @Min(value = 1, message = "Mines cannot be less than 1")
    @Max(value = 399, message = "Mines cannot be greater than 399")
    private int mines;

    @NotNull(message = "User Id cannot be null")
    private long userId;

    public GameBoardInput() {}

    public GameBoardInput(Long userId, int rows, int cols, int mines) {
        this.userId = userId;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
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
