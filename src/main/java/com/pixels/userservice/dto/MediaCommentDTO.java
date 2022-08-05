package com.pixels.userservice.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.MediaComment;
import com.pixels.userservice.model.PixelSenseUser;

public class MediaCommentDTO {
	private String commentId;
	private String commentContent;
	private Date createdAt;
	private String commentByUser;
	private String commentOnMediaId;
	private Set<String> commentLikedBy = new HashSet<>();
	private Set<MediaCommentDTO> commentsOnComment = new HashSet<>();

	public MediaCommentDTO(MediaComment mediaComment) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.createTypeMap(PixelSenseUser.class, String.class).setConverter(context->context.getSource().getUsername());
		modelMapper.createTypeMap(Media.class, String.class).setConverter(context->context.getSource().getMediaId());
		modelMapper.createTypeMap(MediaComment.class, String.class).setConverter(context->context.getSource().getCommentId());
		this.commentId = mediaComment.getCommentId();
		this.commentContent = mediaComment.getCommentContent();
		this.createdAt = mediaComment.getCreatedAt();
		this.commentByUser = mediaComment.getCommentByUser().getUsername();
		this.commentOnMediaId = mediaComment.getCommentOnMediaId().getMediaId();
		this.commentLikedBy = mediaComment.getCommentLikedBy().stream().map(row->row.getUsername()).collect(Collectors.toSet());
		this.commentsOnComment = mediaComment.getCommentsOnComment().stream().map(row->
		modelMapper.map(row, MediaCommentDTO.class)).collect(Collectors.toSet());
	}
	
	@Override
	public String toString() {
		return "MediaCommentDTO [commentId=" + commentId + ", commentContent=" + commentContent + ", createdAt="
				+ createdAt + ", commentByUser=" + commentByUser + ", commentOnMediaId=" + commentOnMediaId
				+ ", commentLikedBy=" + commentLikedBy + ", commentsOnComment=" + commentsOnComment + "]";
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCommentByUser() {
		return commentByUser;
	}

	public void setCommentByUser(String commentByUser) {
		this.commentByUser = commentByUser;
	}

	public String getCommentOnMediaId() {
		return commentOnMediaId;
	}

	public void setCommentOnMediaId(String commentOnMediaId) {
		this.commentOnMediaId = commentOnMediaId;
	}

	public Set<String> getCommentLikedBy() {
		return commentLikedBy;
	}

	public void setCommentLikedBy(Set<String> commentLikedBy) {
		this.commentLikedBy = commentLikedBy;
	}


	public Set<MediaCommentDTO> getCommentsOnComment() {
		return commentsOnComment;
	}

	public void setCommentsOnComment(Set<MediaCommentDTO> commentsOnComment) {
		this.commentsOnComment = commentsOnComment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(commentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaCommentDTO other = (MediaCommentDTO) obj;
		return Objects.equals(commentId, other.commentId);
	}

	public MediaCommentDTO() {
		super();
	}

}
