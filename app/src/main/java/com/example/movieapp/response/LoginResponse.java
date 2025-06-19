package com.example.movieapp.response;

public class LoginResponse {
    private boolean success;
    private String message;
    private String error;
    private long userId;


    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getError() { return error; }

    public long getUserId() {
        return userId;
    }
}