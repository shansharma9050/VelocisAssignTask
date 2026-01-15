package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.SpBootProModel;
import com.example.demo.restcontroller.SpBootProRestController;

@Controller
public class LoginController {
//	@Autowired
//	private SpBootProRestController spBootProRestController;
	
    @GetMapping("/after-varification-login")
    public String afterVarificationlogin() {
        return "login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "email-varification";
    }
    
	/*
	 * @GetMapping("/act") public String getHtmlReturn(Model model) {
	 * 
	 * List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails();
	 * 
	 * model.addAttribute("spBootProModelList",spBootProModelList); return
	 * "/spbootpro"; }
	 */
}