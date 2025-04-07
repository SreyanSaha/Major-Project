package com.help.dto;

public class TokenAuthRequest {
    private String token;
    private String username;
    private short userTypeRole;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public short getUserTypeRole() {
        return userTypeRole;
    }
    public void setUserTypeRole(short userTypeRole) {
        this.userTypeRole = userTypeRole;
    }
}
