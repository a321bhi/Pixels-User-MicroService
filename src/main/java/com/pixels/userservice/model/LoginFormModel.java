package com.pixels.userservice.model;

public class LoginFormModel{
	private String username;
	private String password;
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
	public LoginFormModel(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public LoginFormModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
