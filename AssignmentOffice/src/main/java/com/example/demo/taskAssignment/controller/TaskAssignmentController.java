package com.example.demo.taskAssignment.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.CustomUserPrincipal;
import com.example.demo.assignmentofice.model.AssignmentOfficeModel;
import com.example.demo.assignmentofice.model.DeveloperModel;
import com.example.demo.assignmentofice.model.ProjectModel;
import com.example.demo.feignclient.DeveloperClient;
import com.example.demo.repository.AssignmentOfficeRepository;
import com.example.demo.restController.TaskAssignmentRestController;
import com.example.model.common.model.SpBootProModel;


@Controller
@RequestMapping("/taskassignment")
public class TaskAssignmentController {

	@Autowired
	private TaskAssignmentRestController taskAssignmentRestController;



	@Autowired
	AssignmentOfficeRepository assignmentOfficeRepository;

	@Autowired
	private String apiUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DeveloperClient developerClient;

	@GetMapping("/assignTask")
	public String taskAssign(Model model) throws IOException {

		List<SpBootProModel> devList=	developerClient.getDeveloperList();
		
		List<SpBootProModel> devList1 = devList.stream().filter(a -> a.getRole().equals("Developer") && a.getUserid()!=0)
				.collect(Collectors.toList());
		model.addAttribute("developers", devList1);

		List<ProjectModel> proList = taskAssignmentRestController.getProject();
		model.addAttribute("projects", proList);
		return "task_assignment";
	}

	@GetMapping("/developer")
	public String developer(Model model) throws IOException {
		List<SpBootProModel> devList = developerClient.getDeveloperList();
		List<SpBootProModel> devList1 = devList.stream().filter(a -> a.getRole().equals("Developer") && a.getUserid()!=0)
				.collect(Collectors.toList());
		model.addAttribute("developers", devList1);
		return "developer";
	}

	@GetMapping("/myProfile")
	public String myProfile(   Model model) {
		System.out.print("TaskAssignment===================>");
		
		Authentication auth = SecurityContextHolder
		        .getContext()
		        .getAuthentication();
		
		   CustomUserPrincipal cust =(CustomUserPrincipal) auth.getPrincipal();
		
		  Long userId=cust.getUserId();
		  
		  System.out.print("===============>"+userId);
		  
		  List<AssignmentOfficeModel> myTasks =
		  assignmentOfficeRepository.findByDeveloperId(String.valueOf(userId));
		  
		  model.addAttribute("assignmentOfficeModel", myTasks);
		  
		  SpBootProModel devList = developerClient.getUserById(userId);
		  
		  model.addAttribute("devList", devList);
		 
		return "myProfile";
	}

	@GetMapping("/taskDetails/{id}")
	public String taskDetails(@PathVariable("id") Long id, Model model) throws IOException {
		AssignmentOfficeModel assignmentOfficeModel = taskAssignmentRestController.getTaskDetailsById(id);
		model.addAttribute("taskDetails", assignmentOfficeModel);

		if (assignmentOfficeModel.getCreatedOn() != null) {
			long createdEpoch = assignmentOfficeModel.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant()
					.toEpochMilli();
			model.addAttribute("createdOnEpoch", createdEpoch);
		} else {
			model.addAttribute("createdOnEpoch", null);
		}

		return "taskDetails";
	}

	@GetMapping("/project")
	public String project() {

		return "project";
	}

	@GetMapping("/assignedTask")
	public String task(Model model) throws IOException {

		List<AssignmentOfficeModel> taskDetails = taskAssignmentRestController.getTask();
		model.addAttribute("taskList", taskDetails);

		int totalCount = taskDetails.size();
		int pendingCount = taskDetails.stream().filter(a -> a.getStatus() == 0).collect(Collectors.toList()).size();
		int inProgressCount = taskDetails.stream().filter(a -> a.getStatus() == 1).collect(Collectors.toList()).size();
		int completedCount = taskDetails.stream().filter(a -> a.getStatus() == 2).collect(Collectors.toList()).size();

		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pendingCount", pendingCount);
		model.addAttribute("inProgressCount", inProgressCount);
		model.addAttribute("completedCount", completedCount);

		List<ProjectModel> proList = taskAssignmentRestController.getProject();

		model.addAttribute("projects", proList);
		return "assignedTask";
	}

	@PostMapping("/saveData")
	public String saveData(@ModelAttribute("AssignmentOfficeModel") AssignmentOfficeModel assignmentOfficeModel,
			Model model) throws IOException {
		taskAssignmentRestController.createTask(assignmentOfficeModel);
		model.addAttribute("message", "Data Saved Successfully");

		return "redirect:"+apiUrl+"/taskassignment/myProfile";
	}

	@PostMapping("/saveDeveloper")
	public String saveDeveloper(@ModelAttribute("developer") DeveloperModel developerModel, BindingResult result,
			@RequestParam("resumeFile") MultipartFile resumeFile, Model model) throws IOException {
	
		if (!resumeFile.isEmpty()) {
			String fileName = resumeFile.getOriginalFilename();
			Path uploadPath = Paths.get("uploads/resumes/");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Files.write(uploadPath.resolve(fileName), resumeFile.getBytes());

			developerModel.setResumeFile(fileName); 
		}

		taskAssignmentRestController.createDeveloper(developerModel);
		model.addAttribute("message", "Data Saved Successfully");

		return "redirect:/taskassignment/developer";
	}
	

	@PostMapping("/saveProject")
	public String saveProject(@ModelAttribute("project") ProjectModel projectModel, Model model) throws IOException {
		taskAssignmentRestController.createProject(projectModel);

		taskAssignmentRestController.createProject(projectModel);

		model.addAttribute("message", "Data Saved Successfully");

		return "redirect:/taskassignment/project";
	}
}
