package com.pixelsense.pixelsense.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Media {
	
	@Id
	String mediaId;
	Date mediaDate;
	String mediaEncodedData;
	
	@Override
	public String toString() {
		return "Media [mediaId=" + mediaId + ", mediaDate=" + mediaDate + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(mediaId);
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
		return Objects.equals(mediaId, other.mediaId);
	}
	public Media(String mediaId, Date mediaDate, String mediaEncodedData) {
		super();
		this.mediaId = mediaId;
		this.mediaDate = mediaDate;
		this.mediaEncodedData = mediaEncodedData;
	}
	public Media() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public Date getMediaDate() {
		return mediaDate;
	}
	public void setMediaDate(Date mediaDate) {
		this.mediaDate = mediaDate;
	}
	public String getMediaEncodedData() {
		return mediaEncodedData;
	}
	public void setMediaEncodedData(String mediaEncodedData) {
		this.mediaEncodedData = mediaEncodedData;
	} 
	
}
