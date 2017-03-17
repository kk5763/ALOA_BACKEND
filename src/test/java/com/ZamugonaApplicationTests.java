package com;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.account.AccountDTO;
import com.account.AccountDTO.Create;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ZamugonaApplicationTests {
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	ObjectMapper objectMapper;
	
	MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void contextLoads() {
	}
	@Test
	public void dsa() throws JsonProcessingException, Exception {
		AccountDTO.Create createDTO = new Create();
		createDTO.setUsername("whiteshipe");
		createDTO.setPassword("password");
		
		ResultActions result =	mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDTO)));
		
		result.andDo(print());
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.username", is("whiteship")));
		result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDTO)));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void createAccount_BadRequest() throws JsonProcessingException, Exception{
		AccountDTO.Create createDTO = new Create();
		createDTO.setUsername(" ");
		createDTO.setPassword("123");
		
		ResultActions result =	mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDTO)));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void createAccount_duplicatedUsername(){
		
	}
}
