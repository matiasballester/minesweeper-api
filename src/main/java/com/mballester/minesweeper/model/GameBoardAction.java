package com.mballester.minesweeper.model;

import java.util.Random;

public class GameBoardAction {
    private int row;
    private int col;
    private int rows;
    private int cols;
    private int mines;
    private Cell[][] board;

    public GameBoardAction(int row, int col, Cell[][] board) {
        this.row = row;
        this.col = col;
        this.board = board;
    }

    public GameBoardAction(int rows, int cols, int mines, Cell[][] board) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.board = board;
    }

    public boolean isMined() {
        return board[row][col].isMined();
    }

    public boolean isFlagged() {
        return board[row][col].isFlagged();
    }

    public void flag(){
        board[row][col].setFlagged(true);
    }

    public void unFlag(){
        board[row][col].setFlagged(false);
    }

    public boolean isMined(int rowIndex, int columnIndex) {
        if(isValidRowIndex(rowIndex) && isValidColumnIndex(columnIndex)) {
            return board[rowIndex][columnIndex].isMined();
        }
        return false;
    }

    public void reveal(int rowIndex, int columnIndex){
        if(isValidRowIndex(rowIndex) && isValidColumnIndex(columnIndex)) {
            board[rowIndex][columnIndex].setRevealed(true);
        }
    }

    public void reveal(){
        board[row][col].setRevealed(true);
    }

    public boolean isRevealed() {
        return board[row][col].isRevealed();
    }

    public boolean checkWin() {
        for (int rowIndex=0 ; rowIndex < board.length ; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[0].length; columnIndex++) {
                if (!board[rowIndex][columnIndex].isRevealed() && !board[rowIndex][columnIndex].isMined()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void revealMines(){
        for (int rowIndex=0 ; rowIndex < board.length ; rowIndex++) {
            for (int columnIndex = 0; columnIndex < board[0].length; columnIndex++) {
                if (board[rowIndex][columnIndex].isMined()) {
                    reveal(rowIndex, columnIndex);
                }
            }
        }
    }

    public void revealNeighbors() {
        // Do it for the 8 neighbors cells
        revealNeighbor(row - 1, col - 1);
        revealNeighbor( row - 1, col);
        revealNeighbor(row - 1, col + 1);

        revealNeighbor(row, col - 1);
        revealNeighbor(row, col + 1);

        revealNeighbor(row + 1, col - 1);
        revealNeighbor(row + 1, col);
        revealNeighbor(row + 1, col + 1);
    }

    private void revealNeighbor(int rowIndex, int columnIndex) {
        int boardXIndexMax = board.length - 1;
        int boardYIndexMax = board[0].length - 1;

        // Index out of board
        if(rowIndex < 0 || rowIndex > boardXIndexMax || columnIndex < 0 || columnIndex > boardYIndexMax)
            return;

        if(board[rowIndex][columnIndex].getMinesNear() == 0
            && !board[rowIndex][columnIndex].isMined()
                && !board[rowIndex][columnIndex].isRevealed()) {
            board[rowIndex][columnIndex].setRevealed(true);
        }
    }

    public void loadMines() {
        int index = 0;
        Random random = new Random();
        while(index < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (! board[row][col].isMined()) {
                board[row][col].setMined(true);
            }
            index++;
        }
    }

    public void loadNearMines() {
        for (int r=0; r < rows; r ++) {
            for (int c= 0; c < cols; c++) {
                int nearMines = nearMines(r, c);
                board[r][c].setMinesNear(nearMines);
            }
        }
    }

    private int nearMines(int rowIndex, int columnIndex) {
        int mines = 0;

        //Previous Row
        if(isMined(rowIndex - 1, columnIndex - 1)) mines += 1;
        if(isMined(rowIndex - 1, columnIndex)) mines += 1;
        if(isMined(rowIndex - 1, columnIndex + 1)) mines += 1;

        //Same Row
        if(isMined(rowIndex, columnIndex - 1)) mines += 1;
        if(isMined(rowIndex, columnIndex + 1)) mines += 1;

        //Next Row
        if(isMined(rowIndex + 1, columnIndex - 1)) mines += 1;
        if(isMined(rowIndex + 1, columnIndex)) mines += 1;
        if(isMined(rowIndex + 1, columnIndex + 1)) mines += 1;

        return mines;
    }

    public void flagCell(){
        if(! isRevealed()) {
            if (!isFlagged()) {
                flag();
            } else {
                unFlag();
            }
        }
    }

    private boolean isValidRowIndex(int rowIndex) {
        return rowIndex >= 0 && rowIndex <= board.length - 1;
    }

    private boolean isValidColumnIndex(int columnIndex) {
        return columnIndex >= 0 && columnIndex <= board[0].length - 1;
    }

}
