package com.pixels.userservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.userservice.model.PixelSenseUser;
import com.pixels.userservice.service.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class UserControllerAcceptanceTest {

	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;

	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Autowired
	UserServiceImpl userServiceImpl;

	@Test
	public void shouldGetUserRegistered() throws Exception {
		PixelSenseUser mockUser = new PixelSenseUser();
		mockUser.setUsername("mockuser");
		mockUser.setFullName("Mock User");
		mockUser.setEmailAddress("mockuser@example.com");
		mockUser.setPassword("pass");
		mockUser.setDateOfBirth(
				Date.from(LocalDate.now().plusDays(365 * 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		mockUser.setCountryCode("+91");
		mockUser.setPhoneNumber("1122334455");
		MvcResult result = mockMvc.perform(post("/user/register").content(mapper.writeValueAsString(mockUser))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		assertThat(resultContent).isEqualTo(mockUser.getUsername() + " has registered!");
	}

}
