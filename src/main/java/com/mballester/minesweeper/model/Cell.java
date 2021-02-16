package com.mballester.minesweeper.model;

import java.io.Serializable;

public class Cell implements Serializable {
    private boolean revealed;
    private int minesNear;
    private boolean flagged;
    private boolean mined;
    private boolean questionMark;
    private int index;
    private int row;
    private int col;

    public Cell() {
    }

    public Cell(int index, int row, int col) {
        this.index = index;
        this.row = row;
        this.col = col;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean containsFlagOrQuestionMark() {
        return isFlagged() || isQuestionMark();
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int getMinesNear() {
        return minesNear;
    }

    public void setMinesNear(int minesNear) {
        this.minesNear = minesNear;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isQuestionMark() {
        return questionMark;
    }

    public void setQuestionMark(boolean questionMark) {
        this.questionMark = questionMark;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
