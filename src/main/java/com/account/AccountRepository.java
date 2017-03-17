package com.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{
	Account findByUsername(String username); //jpa 가 알아서 이름으로 유추해서 sql을 만들어준다.
}
