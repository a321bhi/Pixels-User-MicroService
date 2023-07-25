package com.pixels.userservice.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PixelsUserRegistrationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String fullName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String emailAddress;
	private String countryCode;
	private String phoneNumber;
	private Date dateOfBirth;
	private String gender;
	private Date dateOfJoining = new Date();
	private Boolean privacyStatus = false;
	private String profilePicId = "4028818481adc7080181ae0195620001";
	private String password;
	private String profileBio;

	@Override
	public String toString() {
		return "PixelsUserRegistrationDTO [username=" + username + ", fullName=" + fullName + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
				+ ", countryCode=" + countryCode + ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth
				+ ", gender=" + gender + ", dateOfJoining=" + dateOfJoining + ", privacyStatus=" + privacyStatus
				+ ", profilePicId=" + profilePicId + ", password=" + password + ", profileBio=" + profileBio + "]";
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
		PixelsUserRegistrationDTO other = (PixelsUserRegistrationDTO) obj;
		return Objects.equals(username, other.username);
	}

	public PixelsUserRegistrationDTO() {
		super();
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public String getProfilePicId() {
		return profilePicId;
	}

	public void setProfilePicId(String profilePicId) {
		this.profilePicId = profilePicId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileBio() {
		return profileBio;
	}

	public void setProfileBio(String profileBio) {
		this.profileBio = profileBio;
	}

}
