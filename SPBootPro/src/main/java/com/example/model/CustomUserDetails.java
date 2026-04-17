package com.example.model;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class CustomUserDetails implements UserDetails {

	public LoginModel loginModel;
	
	public CustomUserDetails(LoginModel login) {
		this.loginModel=login;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		HashSet<SimpleGrantedAuthority> set=new HashSet<>();
		set.add(new SimpleGrantedAuthority("ROLE_"+loginModel.getRole()));
		
		System.out.println("ROLE FROM DB: " + loginModel.getRole());
		return set;
	}

	@Override
	public String getPassword() {
		return loginModel.getPassword();
	}

	@Override
	public String getUsername() {
		return loginModel.getUsername();
	}
	
	public Long getId(){
		return loginModel.getId();
	}
	
	 public String getRole() {   
	        return loginModel.getRole();
	    }
	 
	 public String getEmail() {   
	        return loginModel.getEmail();
	    }
	
	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }

}
