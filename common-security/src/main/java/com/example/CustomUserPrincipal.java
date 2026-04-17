package com.example;

public class CustomUserPrincipal {

	
	String userName;
	Long userId;
	String role;
	String email;
	
	public CustomUserPrincipal(String userName,Long userId,String role , String email) {
		this.userName=userName;
		
		this.userId=userId;
		this.role=role;
		this.email=email;
	}
	
	public String getUserName() {
		return userName;
	}
	public Long getUserId() {
		return userId;
	}
	public String getRole() {
		return role;
	}
	
    public String getEmail() {
        return email;
    }
}
