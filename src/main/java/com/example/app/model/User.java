package com.example.app.model;


public class User {
    private int id;
    private String email;
    private String username;
    private String fullName;
    private String role;
    private boolean verified;

    public User() {
    }

    public User(int id, String email, String username, String fullName, String role, boolean verified) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}