package com.pixels.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixels.userservice.doa.MediaRepository;
import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.PixelSenseUser;

@Service
public class MediaServiceImpl implements MediaService{

	@Autowired
	MediaRepository mediaRepository;
	
	@Override
	public List<String> getAllMediaOfOneUser(PixelSenseUser user) {
		return mediaRepository.findAllMediaOfUser(user);
	}

	@Override
	public Optional<Media> findMediaById(String mediaId) {
		return mediaRepository.findById(mediaId);
	}

	@Override
	public Media addMedia(Media media) {
		return mediaRepository.save(media);
	}

	@Override
	public void deleteMediaById(String mediaId) {
		mediaRepository.deleteById(mediaId);
	}

	@Override
	public void deleteMedia(Media media) {
		mediaRepository.delete(media);
	}

}
