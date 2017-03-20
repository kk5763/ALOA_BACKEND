package com.account;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.AccountDTO.Update;

@Service
@Transactional //이거 붙여주면 이 안에 퍼블릭 메소드는 모두 트렌젝션 어노테이션이 적용이 된다. 스프링 트랜잭션임
//@Slf4j  //이 어노테이션을 붙여주면 log라는 이름을 가진 Logger 타입의 변수 생성. 한다는데 안되네? 레퍼런스에도 없음. 구글검색결과 없음.
public class AccountService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountRepository accountRepository;
		
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Account createAccount(AccountDTO.Create dto) {
		/*Account account = new Account();
		account.setUsername(dto.getUsername());
		account.setPassword(dto.getPassword());*/
		Account account = modelMapper.map(dto, Account.class);
		
		/*Account account = new Account();
		BeanUtils.copyProperties(dto, account);*/
		
		
		
		
		// 유효한 username인지 판단
		String username = dto.getUsername();
		if(accountRepository.findByUsername(username)!=null){
			logger.error("AccountService.createAccount(): 이미 존재함.");
			throw new UserDuplicatedException(username);
		}
		// password 해싱
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		Date now = new Date();
		account.setJoined(now);
		account.setUpdate(now);
		
		return accountRepository.save(account);
	}
	public Account updateAccount(Long id, AccountDTO.Update updateDTO) {
		Account account = getAccount(id);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setFullName(updateDTO.getFullname());
		return accountRepository.save(account);
		
	}
	public Account getAccount(Long id) {
		Account account = accountRepository.findOne(id);
		if(account==null){
			throw new AccountNotFoundException(id);
		}
		return account;
	}
	public void deleteAccount(long id) {
		accountRepository.delete(getAccount(id));
	}
}

//모델매퍼 검색해보쟝.