package com.mballester.minesweeper.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserInputRequest {
    @NotBlank(message = "Name cannot be empty")
    @Size(min=3, message="Name should have at least 3 characters")
    private String userName;

    @NotBlank(message = "Password cannot be empty")
    @Size(min=6, max=30, message = "Password should have between 6 and 30 characters")
    private String password;

    public UserInputRequest() {
    }

    public UserInputRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
