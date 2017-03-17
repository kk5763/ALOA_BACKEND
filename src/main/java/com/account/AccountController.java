package com.account;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commons.ErrorResponse;

@RestController
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value="/accounts", method=RequestMethod.POST)
	public ResponseEntity createAccount(@RequestBody @Valid AccountDTO.Create dto,
										BindingResult result){
		if(result.hasErrors()){
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setMessage("잘못된 요청입니다.");
			errorResponse.setCode("bad. request");
			//TODO BindingResult 안에 들어있는 에러 정보 사용하기.
			return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
		}
		//여기까지 넘어오면 무조건 잘 됐다고 생각하면 됨 . accountService의 유요한 username판단을 통과했기때문.
		Account newAccount = service.createAccount(dto);
		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDTO.Respose.class), HttpStatus.CREATED);
	}
	
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(e.getUsername()+" 중복 이메일 입니다.");
		errorResponse.setCode("duplicated.username.exception");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}


//requestbody 는 원래 modelAttribute, requestParam 을 많이 썼었는데 요새는 서버사이드 개발이 주로 rest api 쪽으로 가다보니 request 본문에 들어오는걸 파싱받는게 많아졌다.
// 그래서 requestbody를 주로 쓰는데 이걸쓰면 메세지컨버터가 동작을 한다.
// 메세지컨버터중에 하나가 리퀘스트 본문에 들어있는 json 데이타를 객체로 바인딩을 해준다. 즉 account 객체에 json 문자열이 들어온다.
