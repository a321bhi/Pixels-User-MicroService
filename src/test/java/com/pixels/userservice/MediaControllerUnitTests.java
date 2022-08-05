package com.pixels.userservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.MediaComment;
import com.pixels.userservice.model.PixelSenseUser;
import com.pixels.userservice.service.MediaCommentServiceImpl;
import com.pixels.userservice.service.MediaServiceImpl;
import com.pixels.userservice.service.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
class MediaControllerUnitTests {

	private MockMvc mvc;

	@MockBean
	private MediaServiceImpl mediaServiceImpl;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private MediaCommentServiceImpl mediaCommentServiceImpl;

	@Autowired
	private WebApplicationContext context;

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setUpContext() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMedia_whenPostMediaLike_ThenReturnStringResponse() throws Exception {
		Media mockMedia = new Media();
		mockMedia.setMediaId("10");
		Optional<Media> optionalMockMedia = Optional.ofNullable(mockMedia);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		given(mediaServiceImpl.findMediaById(Mockito.any())).willReturn(optionalMockMedia);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);
		mvc.perform(post("/user/media-likes").param("mediaId", "10")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Liked media")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMedia_whenDeleteMediaLike_ThenReturnStringResponse() throws Exception {
		Media mockMedia = new Media();
		mockMedia.setMediaId("10");
		Optional<Media> optionalMockMedia = Optional.ofNullable(mockMedia);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		given(mediaServiceImpl.findMediaById(Mockito.any())).willReturn(optionalMockMedia);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);
		mvc.perform(delete("/user/media-likes/10")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("like removed")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMedia_whenPostMediaComment_ThenReturnStringResponse() throws Exception {
		Media mockMedia = new Media();
		mockMedia.setMediaId("10");
		Optional<Media> optionalMockMedia = Optional.ofNullable(mockMedia);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		given(mediaServiceImpl.findMediaById(Mockito.any())).willReturn(optionalMockMedia);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);
		mvc.perform(post("/user/media-comment").param("mediaId", "10").param("commentContent", "Mock Comment"))
				.andExpect(status().isOk()).andExpect(content().string(equalTo("comment added!")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMediaComment_whenDeleteMediaComment_ThenReturnStringResponse() throws Exception {
		MediaComment mockMediaComment = new MediaComment();
		mockMediaComment.setCommentId("8");

		Optional<MediaComment> optionalMockMediaComment = Optional.ofNullable(mockMediaComment);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		mockMediaComment.setCommentByUser(mockUser);
		given(mediaCommentServiceImpl.findMediaCommentById(Mockito.any())).willReturn(optionalMockMediaComment);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);

		mvc.perform(delete("/user/media-comment/8")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("comment deleted!")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMediaComment_whenDeleteCommentReply_ThenReturnStringResponse() throws Exception {
		MediaComment mockMediaComment = new MediaComment();
		mockMediaComment.setCommentId("50");

		Optional<MediaComment> optionalMockMediaComment = Optional.ofNullable(mockMediaComment);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		mockMediaComment.setCommentByUser(mockUser);
		given(mediaCommentServiceImpl.findMediaCommentById(Mockito.any())).willReturn(optionalMockMediaComment);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);

		mvc.perform(delete("/user/comment-reply/50")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("comment deleted!")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMediaComment_whenPostCommentLike_ThenReturnStringResponse() throws Exception {
		MediaComment mockMediaComment = new MediaComment();
		mockMediaComment.setCommentId("50");

		Optional<MediaComment> optionalMockMediaComment = Optional.ofNullable(mockMediaComment);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		mockMediaComment.setCommentByUser(mockUser);
		given(mediaCommentServiceImpl.findMediaCommentById(Mockito.any())).willReturn(optionalMockMediaComment);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);

		mvc.perform(post("/user/comment-likes").param("commentId", "50")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Liked comment")));
	}

	@Test
	@WithMockUser(username = "mockuser", roles = { "USER" })
	public void givenMediaComment_whenDeleteCommentLike_ThenReturnStringResponse() throws Exception {
		MediaComment mockMediaComment = new MediaComment();
		mockMediaComment.setCommentId("50");

		Optional<MediaComment> optionalMockMediaComment = Optional.ofNullable(mockMediaComment);
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		Optional<PixelSenseUser> optionalMockUser = Optional.ofNullable(mockUser);
		mockMediaComment.setCommentByUser(mockUser);
		given(mediaCommentServiceImpl.findMediaCommentById(Mockito.any())).willReturn(optionalMockMediaComment);
		given(userServiceImpl.findUserById(Mockito.any())).willReturn(optionalMockUser);

		mvc.perform(delete("/user/comment-likes/50")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("comment liked removed")));
	}

}
