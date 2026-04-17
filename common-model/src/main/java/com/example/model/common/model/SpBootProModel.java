package com.example.model.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "userdetails")
public class SpBootProModel {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="userid")
	private Long userid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="username")
	private String username;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phonenumber")
	private Long phonenumber;
	
	@Column(name="empcode")
	private String empcode;
	
	@Column(name="countrycode")
	private String countrycode;
	
	@Column(name="status")
	private String status;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="password")
	private String password;
	
	@Column(name="confirmpassword")
	private String confirmpassword;
	
	@Column(name="isdeleted")
	private Long isdeleted;
	
	@Column(name="role")
	private String role;
}
