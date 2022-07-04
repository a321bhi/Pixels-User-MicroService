package com.pixels.userservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixels.userservice.doa.MediaCommentRepository;
import com.pixels.userservice.model.MediaComment;

@Service
public class MediaCommentServiceImpl implements MediaCommentService {

	@Autowired
	MediaCommentRepository mediaCommentRepository;

	@Override
	public MediaComment addMediaComment(MediaComment mediaComment) {
		MediaComment mediaCommentSaved = mediaCommentRepository.save(mediaComment);
		return mediaCommentSaved;
	}

	@Override
	public Optional<MediaComment> findMediaCommentById(String commentId) {
		return mediaCommentRepository.findById(commentId);
	}

	@Override
	public void deleteMediaCommentById(String commentId) {
		mediaCommentRepository.deleteById(commentId);
		return;
	}

	@Override
	public void deleteMediaComment(MediaComment mediaComment) {
		mediaCommentRepository.delete(mediaComment);
		return;
	}

}
