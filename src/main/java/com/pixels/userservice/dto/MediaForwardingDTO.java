package com.pixels.userservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediaForwardingDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mediaId;
	private Date mediaDate;
	private List<String> mediaTags = new ArrayList<>();
	private String mediaCaption;
	private String imageAsBase64;

	
	@Override
	public String toString() {
		return "ForwardPayload [mediaId=" + mediaId + ", mediaDate=" + mediaDate + ", mediaTags=" + mediaTags
				+ ", mediaCaption=" + mediaCaption + "]";
	}

	public MediaForwardingDTO(String mediaId, Date mediaDate, List<String> mediaTags, String mediaCaption,
			String imageAsBase64) {
		this.setMediaId(mediaId);
		this.mediaDate = mediaDate;
		this.mediaTags = mediaTags;
		this.mediaCaption = mediaCaption;
		this.imageAsBase64 = imageAsBase64;
	}

	public MediaForwardingDTO() {
		super();
	}

	public Date getMediaDate() {
		return mediaDate;
	}

	public void setMediaDate(Date mediaDate) {
		this.mediaDate = mediaDate;
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

	public String getImageAsBase64() {
		return imageAsBase64;
	}

	public void setImageAsBase64(String imageAsBase64) {
		this.imageAsBase64 = imageAsBase64;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
