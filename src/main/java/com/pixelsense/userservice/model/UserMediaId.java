package com.pixelsense.userservice.model;

import java.io.Serializable;
import java.util.Objects;

public class UserMediaId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	private String userId;
	private String mediaId;
	public UserMediaId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserMediaId(String userId, String mediaId) {
		super();
		this.userId = userId;
		this.mediaId = mediaId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(mediaId, userId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserMediaId other = (UserMediaId) obj;
		return Objects.equals(mediaId, other.mediaId) && Objects.equals(userId, other.userId);
	}
	
}
