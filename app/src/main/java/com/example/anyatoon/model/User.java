package com.example.anyatoon.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String userId;

    public User(int id, String username, String password, String userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
