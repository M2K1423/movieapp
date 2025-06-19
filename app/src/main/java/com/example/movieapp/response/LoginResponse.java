package com.example.movieapp.response;

public class LoginResponse {
    private boolean success;
    private String message;
    private String error;
    private long userId;
    private String username;
    private String email;


    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getError() { return error; }

    public long getUserId() {
        return userId;
    }
    // getter
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}