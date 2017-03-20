package com;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.account.Account;
import com.account.AccountDTO;
import com.account.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO test Fixture 객체 적용
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //test에서 트렌젝셔널 속성이 들어가 있으면 데이터베이스가 무조건 롤백됨. 테스트에 최적.
//@Rollback(true)// 이놈이 생략되어 있는거임.
//레퍼런스에 보면 TransactionConfiguration 어노테이션을 @Rollback 으로 바뀌었음.
//이것보단 @Commit 을 레퍼런스에서 확인하자. 더 명시적인듯.
public class ZamugonaApplicationTests {
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AccountService service;
	
	MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Before
	public void setUp(){
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.addFilter(springSecurityFilterChain)
				.build();
	}
	
	@Test
	public void contextLoads() {
	}
	@Test
	public void dsa() throws JsonProcessingException, Exception {
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername("whiteship");
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
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername(" ");
		createDTO.setPassword("123");
		
		ResultActions result =	mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDTO)));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getAccounts() throws Exception{
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername("whiteship");
		createDTO.setPassword("password");
		service.createAccount(createDTO);
		
		ResultActions result = mockMvc.perform(get("/accounts"));
		
		result.andDo(print());
		result.andExpect(status().isOk());
	}
	
	@Test
	public void getAccount() throws Exception{
		
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername("getAccount");
		createDTO.setPassword("getAcc");
		Account account = service.createAccount(createDTO);
		
		ResultActions result = mockMvc.perform(get("/accounts/"+account.getId()));
		result.andDo(print());
		result.andExpect(status().isOk());
	}
	
	@Test
	public void updateAccount() throws JsonProcessingException, Exception{
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername("updateAccount");
		createDTO.setPassword("update");
		Account account = service.createAccount(createDTO);
		
		AccountDTO.Update updateDTO = new AccountDTO.Update();
		updateDTO.setFullname("khk");
		updateDTO.setPassword("updateDTO");
		
		ResultActions result = mockMvc.perform(put("/accounts/"+account.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updateDTO)));
		result.andDo(print());
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.fullName", is("khk")));
	}
	
	@Test
	public void deleteAccount() throws Exception{
		AccountDTO.Create createDTO = new AccountDTO.Create();
		createDTO.setUsername("deleteAccount");
		createDTO.setPassword("delete");
		ResultActions result = mockMvc.perform(delete("/accounts/1")
				.with(httpBasic(createDTO.getUsername(), createDTO.getPassword())));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		
		Account account = service.createAccount(createDTO);
		
		result = mockMvc.perform(delete("/accounts/"+account.getId())
				.with(httpBasic(createDTO.getUsername(), createDTO.getPassword())));
		result.andDo(print());
		result.andExpect(status().isNoContent());
	}
}
