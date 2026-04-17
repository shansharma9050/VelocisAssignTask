package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.CustomUserDetails;
import com.example.model.LoginModel;
import com.example.repository.LoginRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	LoginRepository loginRepository;
	@Override
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {

	    LoginModel login = loginRepository.getByUsername(username);

	    if (login == null) {
	        throw new UsernameNotFoundException("User not found: " + username);
	    }

	    return new CustomUserDetails(login);
	}


}
