package com.example.demo.auth;

public class LoginResponse {
    public String token;
    public long expiresAt; // epoch ms (optional)

    public LoginResponse(String token, long expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }
}
