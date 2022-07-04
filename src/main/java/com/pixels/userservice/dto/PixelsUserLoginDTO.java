package com.pixels.userservice.dto;

public class PixelsUserLoginDTO{
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
	public PixelsUserLoginDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public PixelsUserLoginDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
