package com.pixelsense.pixelsense.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;


@Entity
@IdClass(UserMediaId.class)
public class UserMediaRelation {

	@Id
	String userId;
	@Id
	String mediaId;

	public UserMediaRelation(String userId, String mediaId) {
		super();
		this.userId = userId;
		this.mediaId = mediaId;
	}

	public UserMediaRelation() {
		super();
	}

	@Override
	public String toString() {
		return "UserPostRelation [userId=" + userId + ", postId=" + mediaId + "]";
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
		UserMediaRelation other = (UserMediaRelation) obj;
		return Objects.equals(mediaId, other.mediaId) && Objects.equals(userId, other.userId);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String postId) {
		this.mediaId = postId;
	}

}
