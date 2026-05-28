package com.example.demo.restController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.CustomUserPrincipal;
import com.example.demo.assignmentofice.model.AssignmentOfficeModel;
import com.example.demo.assignmentofice.model.DeveloperModel;
import com.example.demo.assignmentofice.model.ProjectModel;
import com.example.demo.assignmentofice.model.WeeklyPlanModel;
import com.example.demo.feignclient.DeveloperClient;
import com.example.demo.repository.AssignmentOfficeRepository;
import com.example.demo.repository.DeveloperRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.WeeklyPlannerRepository;
import com.example.model.common.model.SpBootProModel;
import com.example.model.repository.SpBootProRepository;

@RestController
@RequestMapping("/home")
public class TaskAssignmentRestController {

	@Autowired
	AssignmentOfficeRepository assignmentOfficeRepository;
	
	@Autowired
	DeveloperClient developerClient;

	@Autowired
	WeeklyPlannerRepository weeklyPlannerRepository;
	
	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	SpBootProRepository spBootProRepository;

	@Autowired
	DeveloperRepository developerRepository;

	public String getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		CustomUserPrincipal cust = (CustomUserPrincipal) auth.getPrincipal();
		String userName = cust.getUserName();
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

	@Transactional
	@PostMapping("/project")
	public ResponseEntity<String> createProject(
			@RequestParam(value = "developers", required = false) List<Long> developerIds,
			@ModelAttribute ProjectModel projectModel) {

		/*
		 * developerIds.forEach(id -> { boolean exists =
		 * spBootProRepository.existsById(id); System.out.println("ID " + id +
		 * " exists in DB? " + exists); });
		 * 
		 * List<SpBootProModel> dvList = developerIds.stream() .map(id ->
		 * spBootProRepository.findById(id).orElse(null)) .filter(Objects::nonNull)
		 * .collect(Collectors.toList());
		 */

		ProjectModel projectModell;

		if (projectModel.getId() != null) {

			projectModell = projectRepository.findById(projectModel.getId()).orElse(new ProjectModel());

			projectModell.setUpdatedOn(new Date());

		} else {

			projectModell = new ProjectModel();
			projectModell.setCreatedOn(new Date());
			projectModell.setCreatedBy(getCurrentUser());
			projectModell.setUpdatedOn(new Date());
		}

		projectModell.setProjectName(projectModel.getProjectName());
		projectModell.setProjectType(projectModel.getProjectType());
		projectModell.setProjectManager(projectModel.getProjectManager());
		projectModell.setProjectOwner(projectModel.getProjectOwner());
		projectModell.setDescription(projectModel.getDescription());
		projectModell.setStartDate(projectModel.getStartDate());
		projectModell.setEndDate(projectModel.getEndDate());
		projectModell.setStatus(projectModel.getStatus());
		projectModell.setBudget(projectModel.getBudget());

		projectModell.setDeveloperIds(developerIds);

		projectRepository.save(projectModell);

		return ResponseEntity.ok("Project saved successfully");
	}

	@GetMapping("/getProject")
	public List<ProjectModel> getProject() throws IOException {

		List<ProjectModel> projectModellList = projectRepository.findAll();

		return projectModellList;
	}

	@GetMapping("/loadProject")
	@ResponseBody
	public List<ProjectModel> getLoadProject(@RequestParam("type") String type) {

		return projectRepository.findByProjectType(type);
	}

	@GetMapping("/getProjectDetails")
	@ResponseBody
	public ProjectModel getProjectDetails(@RequestParam("projectName") String projectName) {

		ProjectModel project = projectRepository.findByProjectName(projectName);

		List<String> developerNames = new ArrayList<>();

		for (Long developerId : project.getDeveloperIds()) {

			SpBootProModel userDtl = developerClient.getDevelopers(developerId);
			String username=userDtl.getUsername();

			developerNames.add(username);
		}
		
		List<AssignmentOfficeModel> task =
	            assignmentOfficeRepository
	            .findByProject(project);
		
		Long totalTask= (long) task.size();
		
		

	    Long resolvedTask =task.stream().filter(a->a.getStatus()== 2).count();

	    project.setTotalTask(totalTask);
	    project.setResolvedTask(resolvedTask);


		project.setDeveloperName(developerNames);

		return project;
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
	
	@PostMapping("/weeklyPlannerSaving")
	public WeeklyPlanModel weeklyPlannerSaving(WeeklyPlanModel weeklyPlanModel) throws IOException {


		return weeklyPlannerRepository.save(weeklyPlanModel);
	}

}
