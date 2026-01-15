package com.example.demo.assignmentofice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="developer")
@Data
public class DeveloperModel {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "full_name", nullable = false, length = 100)
	    private String fullName;

	    @Column(name = "email", nullable = false, unique = true, length = 120)
	    private String email;

	    @Column(name = "phone", length = 20)
	    private String phone;

	    @Column(name = "experience_years")
	    private Double experienceYears;

	    @Column(name = "role", length = 50)
	    private String role;

	    @Column(name = "skills", length = 250)
	    private String skills; // e.g., "Java, Spring Boot, React"

	    @Column(name = "resume_file", length = 255)
	    private String resumeFile; // store resume file path or name
}
