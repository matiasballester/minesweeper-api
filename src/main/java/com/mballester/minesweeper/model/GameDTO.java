package com.mballester.minesweeper.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class GameDTO implements Serializable {
    private long gameId;
    private long userId;
    private Cell[][] board;
    private boolean isActive;
    private boolean userWon;
    private String startTime;
    private String endTime;
    private String status;
    private String timeSpent;
    private Integer rows;
    private Integer cols;
    private Integer mines;
    private String userName;

    private static final DateTimeFormatter YYYYMMDD_HHMMSS = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm:ss");

    public GameDTO(){

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isUserWon() {
        return userWon;
    }

    public void setUserWon(boolean userWon) {
        this.userWon = userWon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public Integer getMines() {
        return mines;
    }

    public void setMines(Integer mines) {
        this.mines = mines;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static GameDTO createGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setUserId(game.getUser().getId());
        gameDTO.setUserName(game.getUser().getUserName());
        gameDTO.setGameId(game.getId());
        gameDTO.setBoard(game.getBoard());
        gameDTO.setCols(game.getCols());
        gameDTO.setRows(game.getRows());
        gameDTO.setMines(game.getMines());
        gameDTO.setActive(game.getState() == States.ACTIVE);
        gameDTO.setUserWon(game.getState() == States.VICTORY);
        gameDTO.setStatus(game.getState().name());
        gameDTO.setStartTime(game.getStartTime().format(YYYYMMDD_HHMMSS));
        if(game.getEndTime() != null) {
            gameDTO.setEndTime(game.getEndTime().format(YYYYMMDD_HHMMSS));

            Duration duration = Duration.between(game.getStartTime(), game.getEndTime());
            StringBuilder timeSpentBuilder = new StringBuilder();

            int hours = (int) Math.floor(duration.getSeconds() / 3600);
            int minutes = (int) Math.floor(duration.getSeconds() % 3600 / 60);
            int seconds = (int) Math.floor(duration.getSeconds() % 3600 % 60);

            if(hours > 0)
                timeSpentBuilder.append(hours > 0 ? hours + (hours == 1 ? " hour, " : " hours, ") : "");
            if(minutes > 0)
                timeSpentBuilder.append(minutes > 0 ? minutes + (minutes == 1 ? " minute, " : " minutes, ") : "");
            if(seconds > 0)
                timeSpentBuilder.append(seconds > 0 ? seconds + (seconds == 1 ? " second " : " seconds ") : "");
            gameDTO.setTimeSpent(timeSpentBuilder.toString());
        }
        return gameDTO;
    }

    public static GameDTO createGameDTO(User user) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setUserId(user.getId());
        return gameDTO;
    }
}
