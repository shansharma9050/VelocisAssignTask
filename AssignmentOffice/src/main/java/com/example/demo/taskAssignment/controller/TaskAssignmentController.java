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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CustomUserPrincipal;
import com.example.demo.assignmentofice.model.AssignmentOfficeModel;
import com.example.demo.assignmentofice.model.DeveloperModel;
import com.example.demo.assignmentofice.model.ProjectModel;
import com.example.demo.assignmentofice.model.WeeklyPlanModel;
import com.example.demo.feignclient.DeveloperClient;
import com.example.demo.repository.AssignmentOfficeRepository;
import com.example.demo.repository.ProjectRepository;
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
	
	@Autowired
	private ProjectRepository projectRepository;

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
	
	@GetMapping("/main_dashboard")
	public String mainDashboard(Model model) throws IOException {
		
		return "main_dashboard";
	}
	
	@GetMapping("/project_management")
	public String projectManagement(Model model) throws IOException {
		
		return "project_management";
	}
	
	@GetMapping("/weekly_planner")
	public String weeklyPlanner(Model model,@ModelAttribute("message") String message) throws IOException {
		
		if(message!=null ) {
			model.addAttribute("message", message);
			 }
		List<ProjectModel> proList = taskAssignmentRestController.getProject();
		model.addAttribute("projects", proList);
		return "weekly_planner";
	}
	
	@GetMapping("/weekly_report")
	public String weekly_report(Model model) throws IOException {
		
		List<ProjectModel> proList = taskAssignmentRestController.getProject();
		model.addAttribute("projects", proList);
		return "weekly_report";
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
	public String project(@ModelAttribute("message") String message,Model model) {
		List<SpBootProModel> devList = developerClient.getDeveloperList();
		List<SpBootProModel> devList1 = devList.stream().filter(a -> a.getRole().equals("Developer") && a.getUserid()!=0)
				.collect(Collectors.toList());
		
		
		 if(message!=null ) {
		model.addAttribute("message", message);
		 }
		model.addAttribute("project", new ProjectModel());
	    model.addAttribute("projectList", projectRepository.findAll());
		model.addAttribute("developers", devList1);
		return "project";
	}

	@GetMapping("/editProject/{id}")
	public String editProject(@PathVariable Long id, Model model) {

		List<SpBootProModel> devList = developerClient.getDeveloperList();
		List<SpBootProModel> devList1 = devList.stream().filter(a -> a.getRole().equals("Developer") && a.getUserid()!=0)
				.collect(Collectors.toList());
		
	    ProjectModel project = projectRepository.findById(id).orElse(null);

	    List<Long> selectedDevIds = project.getDeveloperIds();

	    model.addAttribute("selectedDevIds", selectedDevIds);
	    model.addAttribute("project", project); 
	    model.addAttribute("developers", devList1);
	    model.addAttribute("projectList", projectRepository.findAll());

	    return "project";
	}
	
	@GetMapping("/deleteProject/{id}")
	public String deleteProject(@PathVariable Long id, Model model) {

		List<SpBootProModel> devList = developerClient.getDeveloperList();
		List<SpBootProModel> devList1 = devList.stream().filter(a -> a.getRole().equals("Developer") && a.getUserid()!=0)
				.collect(Collectors.toList());
		
	    projectRepository.deleteById(id);

	    if(id!=null ) {
	    model.addAttribute("message", "Project Deleted Successfully"); 
	    }
	    model.addAttribute("developers", devList1);
	    model.addAttribute("projectList", projectRepository.findAll());

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
	public String saveProject(
	        @RequestParam(value="developers", required = false) List<Long> developerIds,
	        @ModelAttribute("project") ProjectModel projectModel,
	        RedirectAttributes redirectAttributes) {
		
		System.out.println("Incoming IDs=========>: " + developerIds);

		taskAssignmentRestController.createProject(developerIds, projectModel);
		

	    redirectAttributes.addFlashAttribute("message", "Project Saved Successfully");

	    return "redirect:"+apiUrl+"/taskassignment/project";  
	}
	
	@PostMapping("/weeklyPlannerSaving")
	public String weeklyPlannerSaving(
	        @ModelAttribute("WeeklyPlanModel") WeeklyPlanModel weeklyPlanModel,
	        RedirectAttributes redirectAttributes) throws IOException {
		

		taskAssignmentRestController.weeklyPlannerSaving(weeklyPlanModel);
		

	    redirectAttributes.addFlashAttribute("message", "Weekly Plan Saved Successfully");

	    return "redirect:"+apiUrl+"/taskassignment/weekly_planner";  
	}
	
}
