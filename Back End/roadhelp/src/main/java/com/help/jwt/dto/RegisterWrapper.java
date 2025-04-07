package com.help.jwt.dto;

import com.help.model.Admin;
import com.help.model.User;

public class RegisterWrapper {
    private RegisterRequest registerRequest;
    private Admin admin;
    private User user;

    public RegisterRequest getRegisterRequest() {
        return registerRequest;
    }
    public void setRegisterRequest(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
    }
    public Admin getAdmin() {
        return admin;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
