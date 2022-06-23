package com.pixelsense.userservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
	private String phoneNumber;
	private Date dateOfBirth;
	private String gender;
	private Date dateOfJoining = new Date();
	private Boolean privacyStatus;
	private String profilePicId;
	String password;

	public PixelSenseUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@OneToMany(mappedBy = "mediaPostedBy")
	private List<Media> mediaList = new ArrayList<>();

	@ManyToMany(mappedBy = "likedBy")
	private List<Media> likedMedia = new ArrayList<>();

	@OneToMany(mappedBy = "commentByUser")
	private List<MediaComment> commentsOnMedia = new ArrayList<>();

	@ManyToMany(mappedBy = "following")
	private List<PixelSenseUser> follower = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "followers", 
	joinColumns = @JoinColumn(name = "follows"), 
	inverseJoinColumns = @JoinColumn(name = "userId")
	)
	private List<PixelSenseUser> following = new ArrayList<>();

	/* End of declaration of variables and beginning of standard getters/setters, 
	 * constructors, equals, hashcode and toString method
	 * */
	
	
	public String getUserName() {
		return username;
	}

	public PixelSenseUser() {
		super();
		// TODO Auto-generated constructor stub
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

	@Override
	public String toString() {
		return "PixelSenseUser [username=" + username + ", fullName=" + fullName + ", firstName=" + firstName
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

	public List<Media> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<Media> mediaList) {
		this.mediaList = mediaList;
	}

	public List<Media> getLikedMedia() {
		return likedMedia;
	}

	public void setLikedMedia(List<Media> likedMedia) {
		this.likedMedia = likedMedia;
	}

	public List<MediaComment> getCommentsOnMedia() {
		return commentsOnMedia;
	}

	public void setCommentsOnMedia(List<MediaComment> commentsOnMedia) {
		this.commentsOnMedia = commentsOnMedia;
	}

	public List<PixelSenseUser> getFollower() {
		return follower;
	}

	public void setFollower(List<PixelSenseUser> follower) {
		this.follower = follower;
	}

	public List<PixelSenseUser> getFollowing() {
		return following;
	}

	public void setFollowing(List<PixelSenseUser> following) {
		this.following = following;
	}

	
	
	
}
