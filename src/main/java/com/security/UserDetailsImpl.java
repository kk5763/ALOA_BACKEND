package com.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.account.Account;

public class UserDetailsImpl extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDetailsImpl(Account account) {
		super(account.getUsername(), account.getPassword(), authorities(account));
	}

	private static Collection<? extends GrantedAuthority> authorities(Account account) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if(account.getAdmin()!=0){
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return authorities;
	}
}
