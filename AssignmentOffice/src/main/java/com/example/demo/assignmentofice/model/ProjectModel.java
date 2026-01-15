package com.example.demo.assignmentofice.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class ProjectModel {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "project_name", nullable = false, length = 150)
	    private String projectName;

	    @Column(name = "description", length = 500)
	    private String description;

	    @Column(name = "start_date")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date startDate;

	    @Column(name = "end_date")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date endDate;

	    @Column(name = "status", length = 50)
	    private String status; // e.g., Planned, In Progress, Completed

	    @Column(name = "budget")
	    private Double budget;

	    @Column(name = "created_by", length = 100)
	    private String createdBy;

	    @Column(name = "created_on", updatable = false)
	    private Date createdOn;

	    @Column(name = "updated_on")
	    private Date updatedOn;
}
