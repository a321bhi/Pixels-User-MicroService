package com.pixelsense.pixelsense.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PixelSenseUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	String userName;
	String fullName;
	String firstName;
	String middleName;
	String lastName;
	String emailAddress;
	String phoneNumber;
	Date dateOfBirth;
	String gender;
	Date dateOfJoining = new Date();
	Boolean privacyStatus;
	String profilePicId;
	String password;

	public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
	public PixelSenseUser() {
		super();
	}
	public PixelSenseUser(String userName, String fullName, String firstName, String middleName, String lastName,
			String emailAddress, String phoneNumber, Date dateOfBirth, String gender, String password) {
		super();
		this.userName = userName;
		this.fullName = fullName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.password = password;
	}
	public PixelSenseUser(String userName, String fullName, String emailAddress, String phoneNumber, Date dateOfBirth,
			String gender, Date dateOfJoining, Boolean privacyStatus) {
		super();
		this.userName = userName;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.dateOfJoining = dateOfJoining;
		this.privacyStatus = privacyStatus;
		this.middleName="";
		
		String[] arrayOfName = fullName.split(" ", 0);
		int numPartOfName = arrayOfName.length;
		if(numPartOfName>1) {
			this.lastName=arrayOfName[numPartOfName-1];
		}else {
			this.lastName=arrayOfName[0];
		}
		if(numPartOfName>2) {
			int mid = numPartOfName-2;
			int i=1;
			while(mid>0) {
				this.middleName+=arrayOfName[i];
				mid-=1;
				i+=1;
			}
		}
	}
	@Override
	public String toString() {
		return "User [fullName=" + fullName + ", emailAddress=" + emailAddress + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(userName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PixelSenseUser other = (PixelSenseUser) obj;
		return Objects.equals(userName, other.userName);
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
}
