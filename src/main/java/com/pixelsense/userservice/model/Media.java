package com.pixelsense.userservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String mediaId;

	private Date createdAt = new Date();
	@ManyToOne
	private PixelSenseUser mediaPostedBy;

	@ManyToMany
	@JoinTable(name = "mediaLikedBy", joinColumns = @JoinColumn(name = "mediaID"), 
	inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<PixelSenseUser> likedBy = new ArrayList<>();

	@OneToMany(mappedBy = "commentOnMediaId")
	private List<MediaComment> mediaComments = new ArrayList<>();

	public Media() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Media(PixelSenseUser mediaPostedBy, List<PixelSenseUser> likedBy, List<MediaComment> mediaComments) {
		super();
		this.mediaPostedBy = mediaPostedBy;
		this.likedBy = likedBy;
		this.mediaComments = mediaComments;
	}

	@Override
	public String toString() {
		return "Media [mediaPostedBy=" + mediaPostedBy + ", likedBy=" + likedBy + ", mediaComments=" + mediaComments
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, mediaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Media other = (Media) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(mediaId, other.mediaId);
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public PixelSenseUser getMediaPostedBy() {
		return mediaPostedBy;
	}

	public void setMediaPostedBy(PixelSenseUser mediaPostedBy) {
		this.mediaPostedBy = mediaPostedBy;
	}

	public List<PixelSenseUser> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<PixelSenseUser> likedBy) {
		this.likedBy = likedBy;
	}

	public List<MediaComment> getMediaComments() {
		return mediaComments;
	}

	public void setMediaComments(List<MediaComment> mediaComments) {
		this.mediaComments = mediaComments;
	}

	
	
}
