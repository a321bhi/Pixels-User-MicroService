package com.pixels.userservice.service;

import java.util.Optional;

import com.pixels.userservice.model.MediaComment;

public interface MediaCommentService {
	public MediaComment addMediaComment(MediaComment mediaComment);

	public Optional<MediaComment> findMediaCommentById(String commentId);

	public void deleteMediaCommentById(String commentId);
	
	public void deleteMediaComment(MediaComment mediaComment);
}
