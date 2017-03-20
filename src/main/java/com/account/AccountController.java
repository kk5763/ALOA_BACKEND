package com.account;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.commons.ErrorResponse;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value="/accounts", method=POST)
	public ResponseEntity createAccount(@RequestBody @Valid AccountDTO.Create dto,
										BindingResult result){
		if(result.hasErrors()){
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setMessage("잘못된 요청입니다.");
			errorResponse.setCode("bad. request");
			//TODO BindingResult 안에 들어있는 에러 정보 사용하기.
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		//여기까지 넘어오면 무조건 잘 됐다고 생각하면 됨 . accountService의 유요한 username판단을 통과했기때문.
		Account newAccount = service.createAccount(dto);
		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDTO.Respose.class), HttpStatus.CREATED);
	}
	
	//모든 account 를 리턴해주자.
	//metrix variable 방법도 알아보자. 굉장히 복잡한 url 패턴을 파싱하는 방법인데 일단 어떨 때 써야할지 잘 모르겠음.
	//TODO HATEOAS 확인. 이름 누가 이따위로 지었냐.
	@RequestMapping(value="/accounts", method=GET) //paging을 지원하는 기능이 spring data jpa에 들어있다. pagealbe 이라는 메소드가 있다. 
	@ResponseStatus(HttpStatus.OK)
	public PageImpl<AccountDTO.Respose> getAccounts(Pageable pageable) { //pageable 은 spring mvc에 있는 기능인데 이걸 파라미터로 받을 수 있는데 이건 스프링 mvc쪽에 있는 argumentresolver 라는걸 확장해서 spring data jpa가 만들어준다. 이건 스프링데이타웹컨피큐레이션을 등록해줘야 한다. 근데 스프링 부트는 안해도 알아서 됨.(spring data jpa가 등록되어 있어야함)
		// 웹 요청에 만약에 accounts?page=0&size=20%sort=username,asc&sort=joined,desc 이라는 요청이 오면 이렇게 들어오는 파라미터들을 pageable로 받아준다. 굉장히 편한 인터페이스임.
		
		//지금처럼 어떤.. 로직이 필요하지 않다면 굳이 AccountService를 거치지 않고 바로 repository를 호출한다.
		Page<Account> page = accountRepository.findAll(pageable);
		//Account 리턴하면 페스워드가 같이 나감. 그래서 콘텐츠를 받아서 parallelStream() 함수를 사용한다.
		//TODO stream() vs perallelStream() 차이 알아보기.
		List<AccountDTO.Respose> list = page.getContent().parallelStream()
			.map(account -> modelMapper.map(account, AccountDTO.Respose.class))
			.collect(Collectors.toList());
		//page 안에 들어있는 콘텐츠가 account 타입이라 그 콘텐츠의 타입을 전부 하나씩 AccountDTO의 response 타입으로 바꾼거임. 
		
		return new PageImpl<AccountDTO.Respose>(list, pageable, page.getTotalElements());
		//return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value="/accounts/{id}", method=GET)
	@ResponseStatus(HttpStatus.OK) //6->35
	public AccountDTO.Respose getAccount(@PathVariable Long id){
		Account account = service.getAccount(id);
		return modelMapper.map(account, AccountDTO.Respose.class);
	}
	
	//전체 업데이트 (PUT) 구현. username(email주소)는 못(안)바꿈.
	@RequestMapping(value="/accounts/{id}", method=PUT)
	public ResponseEntity updateAccount(@PathVariable Long id,
										@RequestBody @Valid AccountDTO.Update updateDTO,
										BindingResult result){
		if(result.hasErrors()){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		/*Account account = accountRepository.findOne(id);
		if(account == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}*/
		
		Account updateAccount = service.updateAccount(id, updateDTO);
		return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDTO.Respose.class), HttpStatus.OK);
	}
	
	@RequestMapping(value="/accounts/{id}", method=DELETE)
	public ResponseEntity deleteAccount(@PathVariable long id){
		service.deleteAccount(id);
		
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(UserDuplicatedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleUserDuplicatedException(UserDuplicatedException e){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(e.getUsername()+" 중복 이메일 입니다.");
		errorResponse.setCode("duplicated.username.exception");
		return errorResponse;//new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(AccountNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(e.getId()+" 이란 계정이 없습니다.");
		errorResponse.setCode("account.not.found.exception");
		return errorResponse;
	}
	
}

/*@RequestMapping(value="/index", method=GET)
	public String index(Model model){
		model.addAttribute("accounts", accountRepository.findAll());
		return"index"; //index.jsp -> <c:forEach items=${accounts} var="account">
		// ${account.fullName}~~~~~~~${account.password} 이렇게 쓰면 jsp에서 뭘받을지 내가 선택하기 
		//때문에 DTO를 쓸 필요가 없지만 restapi에서는 크롬개발자도구에서 페스워드를 볼 수 있어서 DTO에서 
		//password를 안보내는거다.
	}*/

//requestbody 는 원래 modelAttribute, requestParam 을 많이 썼었는데 요새는 서버사이드 개발이 주로 rest api 쪽으로 가다보니 request 본문에 들어오는걸 파싱받는게 많아졌다.
// 그래서 requestbody를 주로 쓰는데 이걸쓰면 메세지컨버터가 동작을 한다.
// 메세지컨버터중에 하나가 리퀘스트 본문에 들어있는 json 데이타를 객체로 바인딩을 해준다. 즉 account 객체에 json 문자열이 들어온다.
