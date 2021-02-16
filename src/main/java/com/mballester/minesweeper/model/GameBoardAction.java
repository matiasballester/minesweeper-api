package com.mballester.minesweeper.model;

public class GameBoardAction {
    private int row;
    private int col;
    private Cell[][] board;

    public GameBoardAction(int row, int col, Cell[][] board) {
        this.row = row;
        this.col = col;
        this.board = board;
    }

    public boolean isMined() {
        return board[row][col].isMined();
    }

    public boolean isFlagged() {
        return board[row][col].isFlagged();
    }

    public boolean isQuestionMarked(){
        return board[row][col].isQuestionMark();
    }

    public void flag(){
        board[row][col].setFlagged(true);
    }

    public void unFlag(){
        board[row][col].setFlagged(false);
    }

    public void questionMark() {
        board[row][col].setQuestionMark(true);
    }

    public void removeQuestionMark(){
        board[row][col].setQuestionMark(false);
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
                && !board[rowIndex][columnIndex].containsFlagOrQuestionMark()
                    && !board[rowIndex][columnIndex].isRevealed()) {
            board[rowIndex][columnIndex].setRevealed(true);
        }
    }

    public void flagCell(){
        if(! isRevealed()) {
            if (!isFlagged() || isQuestionMarked()) {
                flag();
                if(isQuestionMarked())
                    removeQuestionMark();
            } else {
                unFlag();
            }
        }
    }

    public void questionMarkCell(){
        if(!isRevealed()) {
            if(!isQuestionMarked() || isFlagged()) {
                questionMark();
                if(isFlagged())
                    unFlag();
            } else {
                removeQuestionMark();
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
