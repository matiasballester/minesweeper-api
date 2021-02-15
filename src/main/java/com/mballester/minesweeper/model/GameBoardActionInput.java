package com.mballester.minesweeper.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GameBoardActionInput {
    @NotNull(message = "Game Id cannot be null")
    private long gameId;

    @Min(value = 0, message = "Rows cannot be less than 1")
    @Max(value = 19, message = "Rows cannot be greater than 19")
    private int row;

    @Min(value = 0, message = "Columns cannot be less than 1")
    @Max(value = 19, message = "Columns cannot be greater than 19")
    private int column;

    public GameBoardActionInput() {}

    public GameBoardActionInput(Long gameId, int row, int column) {
        this.gameId = gameId;
        this.row = row;
        this.column = column;
    }

    public GameBoardActionInput(long gameId) {
        this.gameId = gameId;
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

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}
