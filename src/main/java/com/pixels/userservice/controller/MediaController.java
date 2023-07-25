package com.pixels.userservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

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
@RequestMapping("/user")
public class MediaController {

	@Autowired
	private WebClient.Builder webClientBuilder;
	@Value("${application.microservice.media}")
	private String baseUrl;
	private String uriPath="/service/media";
	private String errorResponse = "Error response from Media microservice";
	private String defaultProfilePicId = "4028818481adc7080181ae0195620001";
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
		Media media = new Media();
		media.setMediaPostedBy(user);
		media = mediaServiceImpl.addMedia(media);

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
		MediaRequestDTO forwardPayload = null;
		try {
			forwardPayload = new MediaRequestDTO(media.getMediaId(), media.getCreatedAt(), payload.getMediaTags(),
					payload.getMediaCaption(), Base64.getEncoder().encodeToString(payload.getImage().getBytes()));
		} catch (IOException e) {
			Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			logger.log(Level.SEVERE, "Error in base64 conversion");
		}

		Mono<String> response = webClient.post().uri(uriPath)
				.body(Mono.just(forwardPayload), MediaRequestDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just(errorResponse);
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		return new ResponseEntity<>("data has been added", HttpStatus.OK);

	}

	@PatchMapping("/media-caption")
	public ResponseEntity<String> updateMediaCaption(@RequestBody MediaRequestDTO forwardPayload) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Media> optionalMediaEntity = mediaServiceImpl.findMediaById(forwardPayload.getMediaId());
		Media mediaToBeUpdated;
		if (!optionalMediaEntity.isPresent()) {
			throw new PostNotFoundException();
		} else {
			mediaToBeUpdated = optionalMediaEntity.get();
		}
		if (!mediaToBeUpdated.getMediaPostedBy().getUsername().equals(username)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.patch().uri("/service/media-caption")
				.body(Mono.just(forwardPayload), MediaRequestDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just(errorResponse);
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		return new ResponseEntity<>("Media caption updated successfully.", HttpStatus.OK);
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
		if (!mediaToBeDeleted.getMediaPostedBy().getUsername().equals(username)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}

		mediaToBeDeleted.removeLikedByAndComments();
		mediaServiceImpl.deleteMedia(mediaToBeDeleted);

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.delete().uri("/service/media/" + mediaId).exchangeToMono(res -> {
			if (res.statusCode().equals(HttpStatus.OK)) {
				return res.bodyToMono(String.class);
			} else if (res.statusCode().is4xxClientError()) {
				return Mono.just(errorResponse);
			} else {
				return res.createException().flatMap(Mono::error);
			}
		});
		response.block();
		return new ResponseEntity<>("Media deletion successful.", HttpStatus.OK);
	}

	@GetMapping("/all-media/{username}")
	public List<MediaResponseDTO> getAllMediaOfOneUser(@PathVariable String username) {
		String usernameRequestedBy = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUserById(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		List<MediaResponseDTO> listOfResponsePayload = new ArrayList<>();
		PixelSenseUser user = optionalUser.get();
		if (!usernameRequestedBy.equals(username) || user.getPrivacyStatus() ) {
				return listOfResponsePayload;
		}
		List<String> mediaIdQueryList = mediaServiceImpl.getAllMediaOfOneUser(user);

		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();

		Mono<List<MediaRequestDTO>> response = webClient.post().uri("/service/getAllMedia")
				.body(Mono.just(mediaIdQueryList), new ParameterizedTypeReference<List<String>>() {
				}).retrieve().bodyToMono(new ParameterizedTypeReference<List<MediaRequestDTO>>() {
				});
		List<MediaRequestDTO> listOfForwardPayload = response.block();

		MediaResponseDTO temporaryResponsePayload;
		Optional<Media> tempOptMedia;
		Media tempMedia;
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		modelMapper.createTypeMap(PixelSenseUser.class, String.class)
				.setConverter(context -> context.getSource().getUsername());

		modelMapper.createTypeMap(MediaComment.class, String.class)
				.setConverter(context -> context.getSource().getCommentId());
		for (MediaRequestDTO tempPayload : listOfForwardPayload) {
			tempOptMedia = mediaServiceImpl.findMediaById(tempPayload.getMediaId());
			if (tempOptMedia.isEmpty()) {
				continue;
			}
			tempMedia = tempOptMedia.get();
			temporaryResponsePayload = modelMapper.map(tempMedia, MediaResponseDTO.class);
			temporaryResponsePayload.setMediaTags(tempPayload.getMediaTags());
			temporaryResponsePayload.setMediaCaption(tempPayload.getMediaCaption());
			temporaryResponsePayload.setImageAsBase64(tempPayload.getImageAsBase64());
			listOfResponsePayload.add(temporaryResponsePayload);
		}

		return listOfResponsePayload;
	}

	@PostMapping("/media-likes")
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
		return new ResponseEntity<>("Liked media", HttpStatus.OK);
	}

	@DeleteMapping("/media-likes/{mediaId}")
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
		return new ResponseEntity<>("like removed", HttpStatus.OK);
	}

	@PostMapping("/comment-reply")
	public ResponseEntity<String> replyToComment(@RequestParam String commentId, @RequestParam String commentContent) {
		Optional<MediaComment> mediaCommentOpt = mediaCommentServiceImpl.findMediaCommentById(commentId);
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
		return new ResponseEntity<>("comment added!", HttpStatus.OK);
	}

	@PostMapping("/media-comment")
	public ResponseEntity<String> postComment(@RequestParam String mediaId, @RequestParam String commentContent) {
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
		return new ResponseEntity<>("comment added!", HttpStatus.OK);
	}

	@DeleteMapping("/media-comment/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable String commentId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<MediaComment> optionalOutput = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!optionalOutput.isPresent()) {
			throw new CommentNotFound();
		}
		MediaComment mediaComment = optionalOutput.get();
		if (!username.equals(mediaComment.getCommentByUser().getUsername())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		mediaCommentServiceImpl.deleteMediaComment(mediaComment);
		return new ResponseEntity<>("comment deleted!", HttpStatus.OK);
	}

	@DeleteMapping("/comment-reply/{commentId}")
	public ResponseEntity<String> deleteReplyToComment(@PathVariable String commentId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<MediaComment> commentOpt = mediaCommentServiceImpl.findMediaCommentById(commentId);
		if (!commentOpt.isPresent()) {
			throw new CommentNotFound();
		}
		MediaComment mediaComment = commentOpt.get();
		if (!username.equals(mediaComment.getCommentByUser().getUsername())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		mediaCommentServiceImpl.deleteMediaComment(mediaComment);
		return new ResponseEntity<>("comment deleted!", HttpStatus.OK);
	}

	@PostMapping(value = "/profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

		MediaRequestDTO forwardPayload = null;
		try {
			forwardPayload = new MediaRequestDTO(mediaId, profilePicUpdateDate, payload.getMediaTags(),
					payload.getMediaCaption(), Base64.getEncoder().encodeToString(payload.getImage().getBytes()));
		} catch (IOException e) {
			Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			logger.log(Level.SEVERE, "Error in base64 conversion");
		}

		Mono<String> response = webClient.post().uri(uriPath)
				.body(Mono.just(forwardPayload), MediaRequestDTO.class).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just(errorResponse);
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		user.setProfilePicId(mediaId);
		userServiceImpl.addUser(user);
		return new ResponseEntity<>("Profile pic updated", HttpStatus.OK);
	}

	@DeleteMapping("/profile-pic")
	public ResponseEntity<String> deleteProfilePic() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> userOpt = userServiceImpl.findUserById(username);
		if (!userOpt.isPresent()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = userOpt.get();
		if (user.getProfilePicId().equals(defaultProfilePicId)) {
			return new ResponseEntity<>("profile pic deleted!", HttpStatus.OK);
		}

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.delete().uri(uriPath + user.getProfilePicId())
				.exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just(errorResponse);
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		user.setProfilePicId(defaultProfilePicId);
		userServiceImpl.addUser(user);
		return new ResponseEntity<>("profile pic deleted!", HttpStatus.OK);
	}

	@PostMapping("/comment-likes")
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
		return new ResponseEntity<>("Liked comment", HttpStatus.OK);
	}

	@DeleteMapping("/comment-likes/{commentId}")
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
		return new ResponseEntity<>("comment liked removed", HttpStatus.OK);
	}

	@GetMapping("/all-mappings")
	public List<MediaResponseDTO> getMappingsForListOfMedia(@PathVariable String[] listOfMediaId) {
		List<MediaResponseDTO> listOfResponsePayload = new ArrayList<>();
		MediaResponseDTO temporaryResponsePayload;
		Optional<Media> tempOptMedia;
		Media tempMedia;
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE).setDeepCopyEnabled(true)
				.setFieldMatchingEnabled(true);
		modelMapper.createTypeMap(PixelSenseUser.class, String.class)
				.setConverter(context -> context.getSource().getUsername());

		modelMapper.createTypeMap(MediaComment.class, String.class)
				.setConverter(context -> context.getSource().getCommentId());

		for (String currentMediaId : listOfMediaId) {
			tempOptMedia = mediaServiceImpl.findMediaById(currentMediaId);
			if (tempOptMedia.isEmpty()) {
				continue;
			}
			tempMedia = tempOptMedia.get();
			temporaryResponsePayload = modelMapper.map(tempMedia, MediaResponseDTO.class);
			listOfResponsePayload.add(temporaryResponsePayload);
		}

		return listOfResponsePayload;
	}

	@GetMapping("/mappings/{queryMediaId}")
	public MediaResponseDTO getMappingsOfMedia(@PathVariable String queryMediaId) {
		MediaResponseDTO responsePayload;
		Optional<Media> tempOptMedia;
		Media tempMedia;
		tempOptMedia = mediaServiceImpl.findMediaById(queryMediaId);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		modelMapper.createTypeMap(PixelSenseUser.class, String.class)
				.setConverter(context -> context.getSource().getUsername());

		modelMapper.createTypeMap(MediaComment.class, String.class)
				.setConverter(context -> context.getSource().getCommentId());
		if (tempOptMedia.isEmpty()) {
			throw new PostNotFoundException();
		}
		tempMedia = tempOptMedia.get();
		responsePayload = modelMapper.map(tempMedia, MediaResponseDTO.class);
		return responsePayload;

	}
}