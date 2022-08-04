package com.pixels.userservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class PixelSenseUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	String username;
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

	@OneToMany(mappedBy = "mediaPostedBy")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<Media> mediaList = new HashSet<>();

	@ManyToMany(mappedBy = "likedBy")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<Media> likedMedia = new HashSet<>();

	@ManyToMany(mappedBy = "commentLikedBy")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<MediaComment> commentsLiked = new HashSet<>();

	@OneToMany(mappedBy = "commentByUser")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<MediaComment> commentsOnMedia = new HashSet<>();

	@ManyToMany(mappedBy = "following",fetch = FetchType.EAGER)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<PixelSenseUser> follower = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "followers", joinColumns = @JoinColumn(name = "follows"), inverseJoinColumns = @JoinColumn(name = "userId"))
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<PixelSenseUser> following = new HashSet<>();

	/*
	 * End of declaration of variables and beginning of standard getters/setters,
	 * constructors, equals, hashcode and toString method
	 */

	public String getProfileBio() {
		return profileBio;
	}

	public void setProfileBio(String profileBio) {
		this.profileBio = profileBio;
	}

	public PixelSenseUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUserName() {
		return username;
	}

	public PixelSenseUser() {
		super();
	}

	public PixelSenseUser(String username, String fullName, String firstName, String middleName, String lastName,
			String emailAddress, String phoneNumber, Date dateOfBirth, String gender, String password) {
		super();
		this.username = username;
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

	public PixelSenseUser(String username) {
		this.username = username;

	}

	@Override
	public String toString() {
		return "PixelsUser [username=" + username + ", fullName=" + fullName + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
				+ ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender
				+ ", dateOfJoining=" + dateOfJoining + ", privacyStatus=" + privacyStatus + ", profilePicId="
				+ profilePicId + ", password=" + password + ", mediaList=" + mediaList + ", likedMedia=" + likedMedia
				+ ", commentsOnMedia=" + commentsOnMedia + ", follower=" + follower + ", following=" + following + "]";
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
		PixelSenseUser other = (PixelSenseUser) obj;
		return Objects.equals(username, other.username);
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

	public Set<Media> getMediaList() {
		return mediaList;
	}

	public void setMediaList(Set<Media> mediaList) {
		this.mediaList = mediaList;
	}

	public Set<Media> getLikedMedia() {
		return likedMedia;
	}

	public void setLikedMedia(Set<Media> likedMedia) {
		this.likedMedia = likedMedia;
	}

	public Set<MediaComment> getCommentsOnMedia() {
		return commentsOnMedia;
	}

	public void setCommentsOnMedia(Set<MediaComment> commentsOnMedia) {
		this.commentsOnMedia = commentsOnMedia;
	}

	public Set<PixelSenseUser> getFollower() {
		return follower;
	}

	public void setFollower(Set<PixelSenseUser> follower) {
		this.follower = follower;
	}

	public Set<PixelSenseUser> getFollowing() {
		return following;
	}

	public void setFollowing(Set<PixelSenseUser> followingSet) {
		this.following = followingSet;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
