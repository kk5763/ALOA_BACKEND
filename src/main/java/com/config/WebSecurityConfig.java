package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.security.UserDetailsServiceImpl;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); //이거 켜져있으면 GET 이외의 토큰이 들어올때 csrf토큰값이 들어와서 복잡해짐.
		http.httpBasic();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/accounts/**").hasRole("USER")
			.antMatchers(HttpMethod.PUT, "/accounts/**").hasRole("USER")
			.antMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("USER")
			.anyRequest().permitAll();
	}
}
