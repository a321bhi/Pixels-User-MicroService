package com.pixels.userservice.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.pixels.userservice.model.PixelSenseUser;

public class PixelsUserDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String username;
	private String fullName;
	private String emailAddress;
	private String countryCode;
	private String phoneNumber;
	private Date dateOfBirth;
	private String gender;
	private Date dateOfJoining;
	private Boolean privacyStatus;
	private String profilePicAsBase64;
	private String profileBio;
	private Set<String> follower = new HashSet<>();
	private Set<String> following = new HashSet<>();
	public String getProfilePicAsBase64() {
		return profilePicAsBase64;
	}

	public void setProfilePicAsBase64(String profilePicAsBase64) {
		this.profilePicAsBase64 = profilePicAsBase64;
	}

	public PixelsUserDTO(String username, String fullName, String emailAddress, String phoneNumber,
			Date dateOfBirth, String gender, Date dateOfJoining, Boolean privacyStatus, String profilePicAsBase64,
			String profileDescription) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.dateOfJoining = dateOfJoining;
		this.privacyStatus = privacyStatus;
		this.profilePicAsBase64 = profilePicAsBase64;
		this.profileBio = profileDescription;
	}


	public PixelsUserDTO() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PixelsUserDTO other = (PixelsUserDTO) obj;
		return Objects.equals(username, other.username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public Boolean getPrivacyStatus() {
		return privacyStatus;
	}

	public void setPrivacyStatus(Boolean privacyStatus) {
		this.privacyStatus = privacyStatus;
	}

	public String getProfileBio() {
		return profileBio;
	}
	public void setProfileBio(String profileBio) {
		this.profileBio = profileBio;
	}
	public Set<String> getFollower() {
		return follower;
	}

	public void setFollower(Set<String> follower) {
		this.follower = follower;
	}

	public Set<String> getFollowing() {
		return following;
	}

	public void setFollowing(Set<String> following) {
		this.following = following;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public PixelsUserDTO(PixelSenseUser responseUser) {
		this.username = responseUser.getUserName();
		this.fullName = responseUser.getFullName();
		this.dateOfBirth = responseUser.getDateOfBirth();
		this.profileBio = responseUser.getProfileBio();
	}
}
