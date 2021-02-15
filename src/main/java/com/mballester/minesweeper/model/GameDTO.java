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

    public static GameDTO createGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setUserId(game.getUser().getId());
        gameDTO.setGameId(game.getId());
        gameDTO.setBoard(game.getBoard());
        gameDTO.setActive(game.getState() == States.ACTIVE);
        gameDTO.setUserWon(game.getState() == States.VICTORY);
        gameDTO.setStatus(game.getState().name());
        gameDTO.setStartTime(game.getStartTime().format(YYYYMMDD_HHMMSS));
        if(game.getEndTime() != null) {
            gameDTO.setEndTime(game.getEndTime().format(YYYYMMDD_HHMMSS));

            Duration duration = Duration.between(game.getStartTime(), game.getEndTime());
            StringBuilder timeSpentBuilder = new StringBuilder();
            if(duration.toDays() > 0)
                timeSpentBuilder.append(duration.toDays() + " Days ");
            if(duration.toHours() > 0)
                timeSpentBuilder.append(duration.toHours() + " Hours ");
            if(duration.toMinutes() > 0)
                timeSpentBuilder.append(duration.toMinutes() + " Minutes ");
            if(duration.getSeconds() > 0)
                timeSpentBuilder.append(duration.getSeconds() + " Seconds ");

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
