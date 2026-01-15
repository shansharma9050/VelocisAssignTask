package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.SpBootProModel;
import com.example.demo.restcontroller.SpBootProRestController;


@Controller
@RequestMapping("contra")
public class SpBootProController {

	@Autowired
	private SpBootProRestController spBootProRestController;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	String apiUrl;
	
	@GetMapping("act")
	public String getHtmlReturn(Model model) {
		
		List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails();
		
		model.addAttribute("spBootProModelList",spBootProModelList);
		return "spbootpro";
	}
	
	@GetMapping("/assignTasksp")
	public String redirectToTaskService() {
	    return "redirect:" + apiUrl + "/taskassignment/assignTask";
	}
	
	@GetMapping("registereduser")
	public String getRegisteredUserDetails(Model model) {
		
		List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails();
		
		model.addAttribute("spBootProModelList",spBootProModelList);
		return "registereduser";
	}
	
	@PostMapping("register")
	public String setUserDetails(@ModelAttribute("spBootProModel") SpBootProModel spBootProModel ,Model model) {
		spBootProRestController.saveData(spBootProModel);
		 model.addAttribute("message", "Registration successful");
	        return "success";
	}
	
	
}
