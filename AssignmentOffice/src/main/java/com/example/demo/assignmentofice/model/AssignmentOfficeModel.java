package com.example.demo.assignmentofice.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name="assignmentOffice")
@Data
public class AssignmentOfficeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="taskTitle")
	private String taskTitle;
	
	@ManyToOne
    @JoinColumn(name = "project", referencedColumnName = "id")
	private ProjectModel project;   

	@ManyToOne
	@JoinColumn(name = "developer", referencedColumnName = "id")
	private DeveloperModel developer;  
	
	@Column(name="priority")
	private String priority;
	
	@Column(name="dueDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime dueDate;
	
	@Column(name="taskDetails")
	private String taskDetails;
	
	@Column(name="module")
	private String module;
	
	@Column(name="documentPath")
	private String documentPath;
	
	@Column(name="action")
	private String action;
	
	@Column(name="status")
	private Long status;
	
	@Column(name="createdBy")
	private String createdBy;
	
	@Column(name="createdOn")
	private LocalDateTime createdOn;
	
	@Transient
	private String message;

}
