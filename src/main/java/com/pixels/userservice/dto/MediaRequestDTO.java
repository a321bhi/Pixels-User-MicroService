package com.pixels.userservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MediaRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mediaId;
	private Date createdAt;
	private List<String> mediaTags = new ArrayList<>();
	private String mediaCaption;
	private MultipartFile image;
	private String imageAsBase64;

	public MediaRequestDTO(Date createdAt, List<String> mediaTags, String mediaCaption, MultipartFile image) {
		super();
		this.createdAt = createdAt;
		this.mediaTags = mediaTags;
		this.mediaCaption = mediaCaption;
		this.image = image;
	}

	public MediaRequestDTO(List<String> mediaTags, String mediaCaption, String imageAsBase64) {
		this.mediaTags = mediaTags;
		this.mediaCaption = mediaCaption;
		this.setImageAsBase64(imageAsBase64);
	}

	public MediaRequestDTO(String mediaId, Date createdAt, List<String> mediaTags, String mediaCaption, String imageAsBase64) {
		this.mediaId = mediaId;
		this.createdAt = createdAt;
		this.mediaTags = mediaTags;
		this.mediaCaption = mediaCaption;
		this.setImageAsBase64(imageAsBase64);
	}

	public MediaRequestDTO(String mediaId, Date createdAt, List<String> mediaTags, String mediaCaption, MultipartFile image) {
		this.mediaId = mediaId;
		this.createdAt = createdAt;
		this.mediaTags = mediaTags;
		this.mediaCaption = mediaCaption;
		this.image = image;
	}

	public MediaRequestDTO() {
		super();
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

	public List<String> getMediaTags() {
		return mediaTags;
	}

	public void setMediaTags(List<String> mediaTags) {
		this.mediaTags = mediaTags;
	}

	public String getMediaCaption() {
		return mediaCaption;
	}

	public void setMediaCaption(String mediaCaption) {
		this.mediaCaption = mediaCaption;
	}

	@Override
	public String toString() {
		return "Payload [mediaId=" + mediaId + ", createdAt=" + createdAt + ", mediaTags=" + mediaTags
				+ ", mediaCaption=" + mediaCaption + ", image=" + image + "]";
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getImageAsBase64() {
		return imageAsBase64;
	}

	public void setImageAsBase64(String imageAsBase64) {
		this.imageAsBase64 = imageAsBase64;
	}

}
