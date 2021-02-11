package com.mballester.minesweeper.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Game")
@Table(name="game")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Game {
    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(
            name = "id_generator",
            sequenceName = "id_generator",
            initialValue = 1
    )
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    private States state;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "board")
    private Cell[][] board;

    @Column(name = "start_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    public Game() {}

    public Game(Cell[][] board, String userName) {
        this.board = board;
        this.userName = userName;
        this.state = States.ACTIVE;
        this.startTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public boolean isMined(int row, int col) {
        return board[row][col].isMined();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
