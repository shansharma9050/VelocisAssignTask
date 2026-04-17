package com.example.demo.restController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CustomUserPrincipal;
import com.example.demo.assignmentofice.model.AssignmentOfficeModel;
import com.example.demo.assignmentofice.model.DeveloperModel;
import com.example.demo.assignmentofice.model.ProjectModel;
import com.example.demo.repository.AssignmentOfficeRepository;
import com.example.demo.repository.DeveloperRepository;
import com.example.demo.repository.ProjectRepository;

@RestController
@RequestMapping("/home")
public class TaskAssignmentRestController {

	@Autowired
	AssignmentOfficeRepository assignmentOfficeRepository;

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	DeveloperRepository developerRepository;

	public String getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserPrincipal cust=(CustomUserPrincipal) auth.getPrincipal();
			String userName=cust.getUserName();
		return userName;
	}

	@PostMapping("/tasks")
	public ResponseEntity<String> createTask(AssignmentOfficeModel assignmentOfficeModel) throws IOException {

		AssignmentOfficeModel assign = new AssignmentOfficeModel();

		if (assignmentOfficeModel.getId() != null) {
			assign = assignmentOfficeRepository.findById(assignmentOfficeModel.getId())
					.orElse(new AssignmentOfficeModel());

			assign.setTaskTitle(assign.getTaskTitle());
			assign.setProject(assign.getProject());
			assign.setDeveloper(assign.getDeveloper());
			assign.setPriority(assign.getPriority());
			assign.setDueDate(assign.getDueDate());
			assign.setTaskDetails(assign.getTaskDetails());
			assign.setModule(assign.getModule());
			assign.setStatus(assignmentOfficeModel.getStatus());
			assign.setAction(assignmentOfficeModel.getAction());

			if (assignmentOfficeModel.getDocumentPath() != null && !assignmentOfficeModel.getDocumentPath().isEmpty()) {
				assign.setDocumentPath(assignmentOfficeModel.getDocumentPath());
			}
		} else {

			assign = new AssignmentOfficeModel();
			assign.setCreatedOn(LocalDateTime.now());
		    assign.setCreatedBy(getCurrentUser());
			assign.setTaskTitle(assignmentOfficeModel.getTaskTitle());
			assign.setProject(assignmentOfficeModel.getProject());
			assign.setDeveloperId(assignmentOfficeModel.getDeveloperId());
			assign.setPriority(assignmentOfficeModel.getPriority());
			assign.setDueDate(assignmentOfficeModel.getDueDate());
			assign.setTaskDetails(assignmentOfficeModel.getTaskDetails());
			assign.setModule(assignmentOfficeModel.getModule());
			assign.setStatus(assignmentOfficeModel.getStatus());
			assign.setAction(assignmentOfficeModel.getAction());

			if (assignmentOfficeModel.getDocumentPath() != null && !assignmentOfficeModel.getDocumentPath().isEmpty()) {
				assign.setDocumentPath(assignmentOfficeModel.getDocumentPath());
			}
		}
		assignmentOfficeRepository.save(assign);

		return ResponseEntity.ok("Task created successfully");
	}

	@PostMapping("/developer")
	public ResponseEntity<String> createDeveloper(DeveloperModel developerModel) throws IOException {

		developerRepository.save(developerModel);

		return ResponseEntity.ok("Task created successfully");
	}

	@GetMapping("/getDeveloper")
	public List<DeveloperModel> getDeveloper() throws IOException {

		List<DeveloperModel> developerModelList = developerRepository.findAll();

		return developerModelList;
	}

	@PostMapping("/project")
	public ResponseEntity<String> createProject(ProjectModel projectModel) throws IOException {

		projectRepository.save(projectModel);

		return ResponseEntity.ok("Task created successfully");
	}

	@GetMapping("/getProject")
	public List<ProjectModel> getProject() throws IOException {

		List<ProjectModel> projectModellList = projectRepository.findAll();

		return projectModellList;
	}

	@GetMapping("/getTask")
	public List<AssignmentOfficeModel> getTask() throws IOException {

		List<AssignmentOfficeModel> taskModellList = assignmentOfficeRepository.findAll();

		return taskModellList;
	}

	@GetMapping("/getTaskDetailsById")
	public AssignmentOfficeModel getTaskDetailsById(Long id) throws IOException {

		AssignmentOfficeModel taskModell = assignmentOfficeRepository.findById(id).orElse(null);

		return taskModell;
	}
	
	
}
