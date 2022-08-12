package com.example.spring.security.jwtauth.demo.jwt;

public class JwtUsernamePasswordRequest {
    private String username;
    private String password;

    public JwtUsernamePasswordRequest() {
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
}
