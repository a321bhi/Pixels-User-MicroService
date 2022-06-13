package com.pixelsense.pixelsense.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixelsense.pixelsense.exception.UserSearchEmptyResult;
import com.pixelsense.pixelsense.model.Media;
import com.pixelsense.pixelsense.repository.PostRepository;
import com.pixelsense.pixelsense.service.UserServiceImpl;

@RestController
@RequestMapping("/media")
public class MediaController {
	private Long mediaIdIncrementor=0L;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private PostRepository postRepository;
	@GetMapping("/all")
	public ArrayList<byte[]> getAllMedia() {
		List<Media> databaseOutput =  (List<Media>) postRepository.findAll();
		ArrayList<byte[]> searchResult = new ArrayList();
		for(Media item : databaseOutput) {
			searchResult.add((byte[])Base64.getDecoder().decode(item.getMediaEncodedData()));
		}
		if(searchResult.isEmpty()) {
			throw new UserSearchEmptyResult();
		}else {
			return searchResult;
		}
	}

	// REST End point local
	@PostMapping("/add")
	public ResponseEntity<String> uploadMedia(@RequestBody byte[] file) {
		Media inputMedia = new Media();
		inputMedia.setMediaEncodedData(Base64.getEncoder().encodeToString(file));
		inputMedia.setMediaDate(new Date());
		inputMedia.setMediaId(mediaIdIncrementor.toString());
		mediaIdIncrementor++;
		postRepository.save(inputMedia);
		return new ResponseEntity<String>("data has been added", HttpStatus.OK);
	}
//	
//	@GetMapping("/{userName}")
//	public PixelSenseUser getUserInfo(@PathVariable String userName) {
//		Optional<PixelSenseUser> opt = userService.findUser(userName);
//		if(!opt.isPresent()) {
//			throw new UserNameNotFoundException();
//		}
//		return opt.get();
//	}
//
//	@DeleteMapping("/delete/{userName}")
//	public ResponseEntity<String> deleteUser(@PathVariable String userName) {
//		userService.deleteUser(userName);
//		return new ResponseEntity<String>(userName + " removed successfully", HttpStatus.OK);
//	}
//
//	@PutMapping("/update/{userName}")
//	public ResponseEntity<String> updateUser(@PathVariable String userName, @RequestBody PixelSenseUser updatedUser) {
//		userService.updateUser(userName, updatedUser);
//		return new ResponseEntity<String>(updatedUser.getFirstName() + "'s data has been updated", HttpStatus.OK);
//	}

}
