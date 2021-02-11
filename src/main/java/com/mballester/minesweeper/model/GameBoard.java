package com.mballester.minesweeper.model;

import java.util.Random;

public class GameBoard {
    private int rows;
    private int cols;
    private int mines;
    private Cell[][] board;

    public GameBoard(int rows, int cols, int mines, Cell[][] board) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.board = board;
    }

    public void loadMines() {
        int index = 0;
        Random random = new Random();
        while(index < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            loadMine(row, col);
            index++;
        }
    }

    private void loadMine(int row, int col) {
        if (board[row][col].isMined())
            loadMine(new Random().nextInt(rows), new Random().nextInt(cols));
        else {
            board[row][col].setMined(true);
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

    public boolean isMined(int rowIndex, int columnIndex) {
        if(isValidRowIndex(rowIndex) && isValidColumnIndex(columnIndex)) {
            return board[rowIndex][columnIndex].isMined();
        }
        return false;
    }

    private boolean isValidRowIndex(int rowIndex) {
        return rowIndex >= 0 && rowIndex <= board.length - 1;
    }

    private boolean isValidColumnIndex(int columnIndex) {
        return columnIndex >= 0 && columnIndex <= board[0].length - 1;
    }
}
