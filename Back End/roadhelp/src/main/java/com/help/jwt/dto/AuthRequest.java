package com.help.jwt.dto;

public class AuthRequest {
    private String username;
    private String password;
	private short userTypeRole;
	public short getUserTypeRole() {
		return userTypeRole;
	}
	public void setUserTypeRole(short userTypeRole) {
		this.userTypeRole = userTypeRole;
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
