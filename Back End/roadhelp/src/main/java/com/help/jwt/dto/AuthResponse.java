 package com.help.jwt.dto;

 public class AuthResponse {
     private String token;
	 private String username;
	 private short userTypeRole;
	 public AuthResponse(String token, String username, short userTypeRole) {
		 this.token = token;
		 this.username = username;
		 this.userTypeRole = userTypeRole;
	 }
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
 }
