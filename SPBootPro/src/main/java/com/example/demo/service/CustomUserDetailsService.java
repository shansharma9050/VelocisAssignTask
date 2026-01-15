package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.LoginModel;
import com.example.demo.repository.LoginRepository;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	LoginRepository loginRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginModel login=loginRepository.getByUsername(username);
		return new CustomUserDetails(login);
	}

}
