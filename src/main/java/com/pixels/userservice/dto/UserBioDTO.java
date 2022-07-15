package com.pixels.userservice.dto;

public class UserBioDTO {

	private String bio;

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public UserBioDTO(String bio) {
		super();
		this.bio = bio;
	}

	public UserBioDTO() {
		super();
	}
	
}
