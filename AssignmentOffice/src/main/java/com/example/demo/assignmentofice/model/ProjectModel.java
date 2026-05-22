package com.example.demo.assignmentofice.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.model.common.model.SpBootProModel;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	    
	    @Column(name = "project_manager", length = 150)
	    private String projectManager;
	    
	    @Column(name = "project_owner", length = 150)
	    private String projectOwner;
	    
	    @ElementCollection
	    @CollectionTable(
	        name = "project_developers",
	        joinColumns = @JoinColumn(name = "project_id")
	    )
	    @Column(name = "developer_id")
	    private List<Long> developerIds = new ArrayList<>();
	    
	    @Transient
	    private List<String> developerName = new ArrayList<>();
	    
	    @Column(name = "start_date")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private LocalDate startDate;

	    @Column(name = "end_date")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private LocalDate endDate;

	    @Column(name = "status", length = 50)
	    private String status; // e.g., Planned, In Progress, Completed

	    @Column(name = "projectType", length = 100)
	    private String projectType;
	    
	    @Column(name = "budget")
	    private Double budget;

	    @Column(name = "created_by", length = 100)
	    private String createdBy;

	    @Column(name = "created_on", updatable = false)
	    private Date createdOn;

	    @Column(name = "updated_on")
	    private Date updatedOn;
	    
	    @Transient
	    private Long totalTask;

	    @Transient
	    private Long resolvedTask;
	    
	    
}
