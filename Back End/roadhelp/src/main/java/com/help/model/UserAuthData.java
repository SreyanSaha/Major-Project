package com.help.model;

import jakarta.persistence.*;

@Entity
public class UserAuthData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authId;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private short userTypeRole;//1 -> Admin, 0-> User

    public UserAuthData() {}

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
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

    public short getUserTypeRole() {
        return userTypeRole;
    }

    public void setUserTypeRole(short userTypeRole) {
        this.userTypeRole = userTypeRole;
    }
}
