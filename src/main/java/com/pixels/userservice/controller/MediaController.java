package com.pixels.userservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.pixels.userservice.dto.MediaForwardingDTO;
import com.pixels.userservice.dto.MediaRequestDTO;
import com.pixels.userservice.dto.MediaResponseDTO;
import com.pixels.userservice.exception.CommentNotFound;
import com.pixels.userservice.exception.PostNotFoundException;
import com.pixels.userservice.exception.UsernameNotFoundException;
import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.MediaComment;
import com.pixels.userservice.model.PixelSenseUser;
import com.pixels.userservice.service.MediaCommentServiceImpl;
import com.pixels.userservice.service.MediaServiceImpl;
import com.pixels.userservice.service.UserServiceImpl;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
public class MediaController {

	@Autowired
	private WebClient.Builder webClientBuilder;
	private String baseUrl = "http://mediaservice";

	@Autowired
	MediaServiceImpl mediaServiceImpl;

	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	MediaCommentServiceImpl mediaCommentServiceImpl;

	@PostMapping(value = "/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadMedia(@ModelAttribute MediaRequestDTO payload) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		Media media = new Media(new Date(), user);
		media = mediaServiceImpl.addMedia(media);

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		MediaForwardingDTO forwardPayload = null;
		try {
			forwardPayload = new MediaForwardingDTO(media.getMediaId(), media.getCreatedAt(), payload.getMediaTags(),
					payload.getMediaCaption(), Base64.getEncoder().encodeToString(payload.getImage().getBytes()));
		} catch (IOException e) {
			System.out.print("ERROR in base64 conv");
		}

		Mono<String> response = webClient.post().uri("/service/media")
				.body(Mono.just(forwardPayload), MediaForwardingDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just("Error response");
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		return new ResponseEntity<String>("UCResponse - data has been added", HttpStatus.OK);

	}

	@PatchMapping("/media-caption")
	public ResponseEntity<String> updateMediaDetails(@RequestBody MediaForwardingDTO forwardPayload) {
//		System.out.println(forwardPayload.toString());
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Media> optionalMediaEntity = mediaServiceImpl.findMediaById(forwardPayload.getMediaId());
		Media mediaToBeUpdated;
		if (!optionalMediaEntity.isPresent()) {
			throw new PostNotFoundException();
		} else {
			mediaToBeUpdated = optionalMediaEntity.get();
		}
		if (!mediaToBeUpdated.getMediaPostedBy().getUserName().equals(username)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.patch().uri("/service/media-caption")
				.body(Mono.just(forwardPayload), MediaForwardingDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just("Error response");
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		return new ResponseEntity<String>("Media caption updated successfully.", HttpStatus.OK);
	}

	@DeleteMapping("/media/{mediaId}")
	public ResponseEntity<String> deleteOneMedia(@PathVariable String mediaId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Media> optionalMediaEntity = mediaServiceImpl.findMediaById(mediaId);
		Media mediaToBeDeleted;
		if (!optionalMediaEntity.isPresent()) {
			throw new PostNotFoundException();
		} else {
			mediaToBeDeleted = optionalMediaEntity.get();
		}
		if (!mediaToBeDeleted.getMediaPostedBy().getUserName().equals(username)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		
		mediaToBeDeleted.removeLikedByAndComments();
		mediaServiceImpl.deleteMedia(mediaToBeDeleted);
		
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.delete().uri("/service/media/"+ mediaId).exchangeToMono(res -> {
			if (res.statusCode().equals(HttpStatus.OK)) {
				return res.bodyToMono(String.class);
			} else if (res.statusCode().is4xxClientError()) {
				return Mono.just("Error response");
			} else {
				return res.createException().flatMap(Mono::error);
			}
		});
		response.block();
		return new ResponseEntity<String>("Media deletion successful.", HttpStatus.OK);
	}

	@GetMapping("/media/all/{username}")
	public List<MediaResponseDTO> getAllMediaOfOneUser(@PathVariable String username) {
		String usernameRequestedBy = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		List<MediaResponseDTO> listOfResponsePayload = new ArrayList<>();
		PixelSenseUser user = optionalUser.get();
		if (!usernameRequestedBy.equals(username)) {
			if (user.getPrivacyStatus()) {
				return listOfResponsePayload;
			}
		}
		List<String> mediaIdQueryList = mediaServiceImpl.getAllMediaOfOneUser(user);

		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();

		Mono<List<MediaForwardingDTO>> response = webClient.post().uri("/service/getAllMedia")
				.body(Mono.just(mediaIdQueryList), new ParameterizedTypeReference<List<String>>() {
				}).retrieve().bodyToMono(new ParameterizedTypeReference<List<MediaForwardingDTO>>() {
				});
		List<MediaForwardingDTO> listOfForwardPayload = (List<MediaForwardingDTO>) response.block();

		MediaResponseDTO temporaryResponsePayload;
		Optional<Media> tempOptMedia;
		Media tempMedia;
		for (MediaForwardingDTO tempPayload : listOfForwardPayload) {
			Set<String> usersWhoLikedThisMedia = new HashSet<>();
			temporaryResponsePayload = new MediaResponseDTO(tempPayload);
			tempOptMedia = mediaServiceImpl.findMediaById(temporaryResponsePayload.getMediaId());
			tempMedia = tempOptMedia.get();
			for (PixelSenseUser tempUser : tempMedia.getLikedBy()) {
				usersWhoLikedThisMedia.add(tempUser.getUserName());
			}
			temporaryResponsePayload.setLikedBy(usersWhoLikedThisMedia);
			temporaryResponsePayload.setMediaComments(tempMedia.getMediaComments());
			temporaryResponsePayload.refactorMediaComments();
			listOfResponsePayload.add(temporaryResponsePayload);
		}

		return listOfResponsePayload;
	}

	@PostMapping("/media/likes")
	public ResponseEntity<String> likeMedia(@RequestParam String mediaId) {

		Optional<Media> optionalMedia = mediaServiceImpl.findMediaById(mediaId);
		if (!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = media.getLikedBy();
		likedByList.add(user);
		media.setLikedBy(likedByList);
		mediaServiceImpl.addMedia(media);
		return new ResponseEntity<String>("Liked media", HttpStatus.OK);
	}

	@DeleteMapping("/media/likes/{mediaId}")
	public ResponseEntity<String> unlikeMedia(@PathVariable String mediaId) {
		Optional<Media> optionalMedia = mediaServiceImpl.findMediaById(mediaId);
		if (!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = media.getLikedBy();
		if (likedByList.contains(user)) {
			likedByList.remove(user);
			media.setLikedBy(likedByList);
			mediaServiceImpl.addMedia(media);
		}
		return new ResponseEntity<String>("like removed", HttpStatus.OK);
	}

	@PostMapping("/comment/comment")
	public ResponseEntity<String> commentOnComment(@RequestParam String commentId, @RequestParam String commentContent) {
		System.out.println(commentId);
		System.out.println(commentContent);
		Optional<MediaComment> mediaCommentOpt =  mediaCommentServiceImpl.findMediaCommentById(commentId);
//		Optional<Media> optionalMedia = mediaServiceImpl.findMediaById(mediaId);
		if (!mediaCommentOpt.isPresent()) {
			throw new CommentNotFound();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		MediaComment mediaComment = mediaCommentOpt.get();
		PixelSenseUser user = optionalUser.get();
		MediaComment commentOnComment = new MediaComment(commentContent, user, mediaComment);
		mediaCommentServiceImpl.addMediaComment(commentOnComment);
		return new ResponseEntity<String>("comment added!", HttpStatus.OK);
	}
	

	@PostMapping("/media/comment")
	public ResponseEntity<String> commentOnMedia(@RequestParam String mediaId, @RequestParam String commentContent) {
		Optional<Media> optionalMedia = mediaServiceImpl.findMediaById(mediaId);
		if (!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		MediaComment mediaComment = new MediaComment(commentContent, user, media);
		mediaCommentServiceImpl.addMediaComment(mediaComment);
		return new ResponseEntity<String>("comment added!", HttpStatus.OK);
	}

	@DeleteMapping("/media/comment/{commentId}")
	public ResponseEntity<String> commentOnMedia(@PathVariable String commentId) {
	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<MediaComment> optionalOutput = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!optionalOutput.isPresent()) {
			throw new CommentNotFound();
		}
		MediaComment mediaComment = optionalOutput.get();
		if (!username.equals(mediaComment.getCommentByUser().getUserName())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		mediaCommentServiceImpl.deleteMediaComment(mediaComment);
		return new ResponseEntity<String>("comment deleted!", HttpStatus.OK);
	}

	@DeleteMapping("/comment/comment/{commentId}")
	public ResponseEntity<String> deleteCommentOnComment(@PathVariable String commentId) {
	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<MediaComment> commentOpt = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!commentOpt.isPresent()) {
			throw new CommentNotFound();
		}
		MediaComment mediaComment = commentOpt.get();
		if (!username.equals(mediaComment.getCommentByUser().getUserName())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		mediaCommentServiceImpl.deleteMediaComment(mediaComment);
		return new ResponseEntity<String>("comment deleted!", HttpStatus.OK);
	}

	@PostMapping(value = "/media/profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateProfilePic(@ModelAttribute MediaRequestDTO payload) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		String mediaId = UUID.randomUUID().toString();
		Date profilePicUpdateDate = new Date();
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		MediaForwardingDTO forwardPayload = null;
		try {
			forwardPayload = new MediaForwardingDTO(mediaId, profilePicUpdateDate, payload.getMediaTags(),
					payload.getMediaCaption(), Base64.getEncoder().encodeToString(payload.getImage().getBytes()));
		} catch (IOException e) {
			System.out.print("ERROR in base64 conv");
		}

		Mono<String> response = webClient.post().uri("/service/media")
				.body(Mono.just(forwardPayload), MediaForwardingDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just("Error response");
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		user.setProfilePicId(mediaId);
		userServiceImpl.addUser(user);
		System.out.println(mediaId);
		return new ResponseEntity<String>("Profile pic updated", HttpStatus.OK);
	}

	@DeleteMapping("/media/profile-pic")
	public ResponseEntity<String> deleteProfilePic() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> userOpt = userServiceImpl.findUserById(username);
		if (!userOpt.isPresent()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = userOpt.get();
		if (user.getProfilePicId().equals("4028818481adc7080181ae0195620001")) {
			return new ResponseEntity<String>("profile pic deleted!", HttpStatus.OK);
		}

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.delete().uri("/service/media"+ user.getProfilePicId())
				.exchangeToMono(res -> {
			if (res.statusCode().equals(HttpStatus.OK)) {
				return res.bodyToMono(String.class);
			} else if (res.statusCode().is4xxClientError()) {
				return Mono.just("Error response");
			} else {
				return res.createException().flatMap(Mono::error);
			}
		});
		response.block();
		user.setProfilePicId("4028818481adc7080181ae0195620001");
		userServiceImpl.addUser(user);
		return new ResponseEntity<String>("profile pic deleted!", HttpStatus.OK);
	}

	@PostMapping("/media/comment-likes")
	public ResponseEntity<String> likeComment(@RequestParam String commentId) {

		Optional<MediaComment> optionalMediaComment = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!optionalMediaComment.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		MediaComment mediaComment = optionalMediaComment.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = mediaComment.getCommentLikedBy();
		likedByList.add(user);
		mediaComment.setCommentLikedBy(likedByList);
		mediaCommentServiceImpl.addMediaComment(mediaComment);
		return new ResponseEntity<String>("Liked comment", HttpStatus.OK);
	}

	@DeleteMapping("/media/comment-likes/{commentId}")
	public ResponseEntity<String> unlikeComment(@PathVariable String commentId) {
		Optional<MediaComment> optionalMediaComment = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!optionalMediaComment.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		MediaComment mediaComment = optionalMediaComment.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = mediaComment.getCommentLikedBy();
		if (likedByList.contains(user)) {
			likedByList.remove(user);
			mediaComment.setCommentLikedBy(likedByList);
			mediaCommentServiceImpl.addMediaComment(mediaComment);
		}
		return new ResponseEntity<String>("like removed media", HttpStatus.OK);
	}
}