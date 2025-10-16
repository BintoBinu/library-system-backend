package com.example.library.system.dto;

public class AuthResponse {
    private String username;
    private String role;

    public AuthResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }
}