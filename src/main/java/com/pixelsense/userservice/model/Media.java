package com.pixelsense.userservice.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Media {

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String mediaId;

	private Date createdAt = new Date();
	@ManyToOne
	private PixelSenseUser mediaPostedBy;

	@ManyToMany
	@JoinTable(name = "mediaLikedBy", joinColumns = @JoinColumn(name = "mediaID"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private Set<PixelSenseUser> likedBy = new HashSet<>();

	@OneToMany(mappedBy = "commentOnMediaId")
	private Set<MediaComment> mediaComments = new HashSet<>();

	public Media() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Media(Date createdAt, PixelSenseUser mediaPostedBy) {
		super();
		this.createdAt = createdAt;
		this.mediaPostedBy = mediaPostedBy;
	}

	public Media(PixelSenseUser mediaPostedBy, HashSet<PixelSenseUser> likedBy, HashSet<MediaComment> mediaComments) {
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

	public Set<PixelSenseUser> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Set<PixelSenseUser> likedBy) {
		this.likedBy = likedBy;
	}

	public Set<MediaComment> getMediaComments() {
		return mediaComments;
	}

	public void setMediaComments(Set<MediaComment> mediaComments) {
		this.mediaComments = mediaComments;
	}

}
