package com.pixels.userservice.service;

import java.util.List;
import java.util.Optional;

import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.PixelSenseUser;

public interface MediaService {
	public List<String> getAllMediaOfOneUser(PixelSenseUser user);
	public Optional<Media> findMediaById(String mediaId);
	public Media addMedia(Media media);
	public void deleteMedia(Media media);
	public void deleteMediaById(String mediaId);
	
}
