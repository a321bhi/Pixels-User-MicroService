package com.pixelsense.userservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.pixelsense.userservice.doa.MediaCommentRepository;
import com.pixelsense.userservice.doa.MediaRepository;
import com.pixelsense.userservice.exception.PostNotFoundException;
import com.pixelsense.userservice.exception.UsernameNotFoundException;
import com.pixelsense.userservice.model.Media;
import com.pixelsense.userservice.model.MediaComment;
import com.pixelsense.userservice.model.PixelSenseUser;
import com.pixelsense.userservice.payload.ForwardPayload;
import com.pixelsense.userservice.payload.RequestPayload;
import com.pixelsense.userservice.payload.ResponsePayload;
import com.pixelsense.userservice.service.UserServiceImpl;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/media")
public class MediaController {

	@Autowired
	private WebClient.Builder webClientBuilder;
	private String baseUrl = "http://mediaservice";

	@Autowired
	MediaRepository mediaRepository;

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	MediaCommentRepository mediaCommentRepository;

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadMedia(@ModelAttribute RequestPayload payload) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();
		Media media = new Media(new Date(), user);
		media = mediaRepository.save(media);

		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		ForwardPayload forwardPayload = null;
		try {
			forwardPayload = new ForwardPayload(media.getMediaId(), media.getCreatedAt(), payload.getMediaTags(),
					payload.getMediaCaption(), Base64.getEncoder().encodeToString(payload.getImage().getBytes()));
		} catch (IOException e) {
			System.out.print("ERROR in base64 conv");
		}

		Mono<String> response = webClient.post().uri("/media/service/add")
				.body(Mono.just(forwardPayload), ForwardPayload.class).exchangeToMono(res -> {
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
//
//	@PostMapping("/get")
//	public ForwardPayload getMedia(@RequestParam String mediaId) {
//		ExchangeStrategies strategies = ExchangeStrategies.builder()
//				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
//		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();
//
//		Mono<ForwardPayload> response = webClient.post().uri("/media/service/get")
//				.body(BodyInserters.fromFormData("mediaId", mediaId)).retrieve().bodyToMono(ForwardPayload.class);
//		return response.block();
//	}

	@PostMapping("/deleteOneMedia")
	public ResponseEntity<String> deleteOneMedia(@RequestParam String mediaId) {
	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Media> optionalMediaEntity = mediaRepository.findById(mediaId);
		Media mediaToBeDeleted;
		if (!optionalMediaEntity.isPresent()) {
			throw new PostNotFoundException();
		} else {
			mediaToBeDeleted = optionalMediaEntity.get();
		}
		if (!mediaToBeDeleted.getMediaPostedBy().getUserName().equals(username)) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

		Mono<String> response = webClient.post().uri("/media/service/deleteOneMedia")
				.body(BodyInserters.fromFormData("mediaId", mediaId)).exchangeToMono(res -> {
					if (res.statusCode().equals(HttpStatus.OK)) {
						return res.bodyToMono(String.class);
					} else if (res.statusCode().is4xxClientError()) {
						return Mono.just("Error response");
					} else {
						return res.createException().flatMap(Mono::error);
					}
				});
		response.block();
		mediaRepository.delete(mediaToBeDeleted);
		return new ResponseEntity<String>("Media deletion successful.", HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public List<ResponsePayload> getAllMediaOfOneUser(@RequestParam String username) {
		String usernameRequestedBy = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		List<ResponsePayload> listOfResponsePayload = new ArrayList<>();
		PixelSenseUser user = optionalUser.get();
		if(!usernameRequestedBy.equals(username)) {
			if(user.getPrivacyStatus()) {
				return listOfResponsePayload;
			}
		}
		List<String> mediaIdQueryList = mediaRepository.findAllMediaOfUser(user);

		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();

		Mono<List<ForwardPayload>> response = webClient.post().uri("/media/service/getAllMedia")
				.body(Mono.just(mediaIdQueryList), new ParameterizedTypeReference<List<String>>() {
				}).retrieve().bodyToMono(new ParameterizedTypeReference<List<ForwardPayload>>() {
				});
		List<ForwardPayload> listOfForwardPayload =  (List<ForwardPayload>) response.block();

		
		ResponsePayload temporaryResponsePayload;
		Optional<Media> tempOptMedia;
		Media tempMedia;
		for(ForwardPayload tempPayload : listOfForwardPayload) {
			Set<String> usersWhoLikedThisMedia = new HashSet<>();
			temporaryResponsePayload = new ResponsePayload(tempPayload);
			tempOptMedia = mediaRepository.findById(temporaryResponsePayload.getMediaId());
			tempMedia = tempOptMedia.get();
			//usersWhoLikedThisMedia = mediaRepository.findUsersWhoLikedThisMedia(tempMedia);
			for(PixelSenseUser tempUser: tempMedia.getLikedBy()){
				usersWhoLikedThisMedia.add(tempUser.getUserName());
			}
			temporaryResponsePayload.setLikedBy(usersWhoLikedThisMedia);
			temporaryResponsePayload.setMediaComments(tempMedia.getMediaComments());
			listOfResponsePayload.add(temporaryResponsePayload);
		}
		
		return listOfResponsePayload;
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
		Mono<String> response = webClient.get().uri("/media/service/test").exchangeToMono(res -> {
			if (res.statusCode().equals(HttpStatus.OK)) {
				return res.bodyToMono(String.class);
			} else if (res.statusCode().is4xxClientError()) {
				return Mono.just("Error response");
			} else {
				return res.createException().flatMap(Mono::error);
			}
		});

		return new ResponseEntity<String>(response.block().toString(), HttpStatus.OK);
	}
	

	@PostMapping("/likeMedia")
	public ResponseEntity<String> likeMedia(@RequestParam String mediaId) {

		Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
		if(!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = media.getLikedBy();
		likedByList.add(user);
		media.setLikedBy(likedByList);
		mediaRepository.save(media);
		return new ResponseEntity<String>("Liked media", HttpStatus.OK);
	}
	@PostMapping("/unlikeMedia")
	public ResponseEntity<String> unlikeMedia(@RequestParam String mediaId) {
		Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
		if(!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		Set<PixelSenseUser> likedByList = media.getLikedBy();
		if(likedByList.contains(user)) {
			likedByList.remove(user);
			media.setLikedBy(likedByList);
			mediaRepository.save(media);	
		}
		return new ResponseEntity<String>("Liked media", HttpStatus.OK);
	}
	@PostMapping("/commentOnMedia")
	public ResponseEntity<String> commentOnMedia(@RequestParam String mediaId, @RequestParam String commentContent) {
		Optional<Media> optionalMedia = mediaRepository.findById(mediaId);
		if(!optionalMedia.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		Media media = optionalMedia.get();
		PixelSenseUser user = optionalUser.get();
		MediaComment mediaComment = new MediaComment(commentContent,user,media);
		mediaCommentRepository.save(mediaComment);
		return new ResponseEntity<String>("comment added!", HttpStatus.OK);
	}
	@PostMapping("/deleteComment")
	public ResponseEntity<String> commentOnMedia(@RequestParam String commentId) {
		Optional<MediaComment> optionalComment = mediaCommentRepository.findById(commentId);
		if(!optionalComment.isPresent()) {
			throw new PostNotFoundException();
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<MediaComment> optionalOutput =  mediaCommentRepository.findById(commentId);
		if(!optionalOutput.isPresent()) {
			throw new PostNotFoundException();
		}
		MediaComment mediaComment = optionalOutput.get();
		if(!username.equals(mediaComment.getCommentByUser().getUserName())) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}
		mediaCommentRepository.delete(mediaComment);
		return new ResponseEntity<String>("comment deleted!", HttpStatus.OK);
	}
	

}