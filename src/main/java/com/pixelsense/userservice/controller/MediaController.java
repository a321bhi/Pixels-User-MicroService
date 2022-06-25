package com.pixelsense.userservice.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.pixelsense.userservice.doa.MediaRepository;
import com.pixelsense.userservice.exception.PostNotFoundException;
import com.pixelsense.userservice.exception.UsernameNotFoundException;
import com.pixelsense.userservice.model.ForwardPayload;
import com.pixelsense.userservice.model.Media;
import com.pixelsense.userservice.model.Payload;
import com.pixelsense.userservice.model.PixelSenseUser;
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

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadMedia(@ModelAttribute Payload payload) {
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

	@PostMapping("/get")
	public ForwardPayload getMedia(@RequestParam String mediaId) {
		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();

		Mono<ForwardPayload> response = webClient.post().uri("/media/service/get")
				.body(BodyInserters.fromFormData("mediaId", mediaId)).retrieve().bodyToMono(ForwardPayload.class);
		return response.block();
	}

	@PostMapping("/deleteOneMedia")
	public ResponseEntity<String> deleteOneMedia(@RequestParam String mediaId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<Media> optionalMediaEntity = mediaRepository.findById(mediaId);
		Media mediaToBeDeleted;
		if (optionalMediaEntity.isEmpty()) {
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
	public List<ForwardPayload> getAllMediaOfOneUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PixelSenseUser> optionalUser = userServiceImpl.findUser(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException();
		}
		PixelSenseUser user = optionalUser.get();

		List<String> mediaIdQueryList = mediaRepository.findAllMediaOfUser(user);

		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(-1)).build();
		WebClient webClient = webClientBuilder.exchangeStrategies(strategies).baseUrl(baseUrl).build();

		Mono<List<ForwardPayload>> response = webClient.post().uri("/media/service/getAllMedia")
				.body(Mono.just(mediaIdQueryList), new ParameterizedTypeReference<List<String>>() {
				}).retrieve().bodyToMono(new ParameterizedTypeReference<List<ForwardPayload>>() {
				});
		return response.block();
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

}