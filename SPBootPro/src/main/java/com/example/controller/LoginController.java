package com.example.controller;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.CustomUserDetails;
import com.example.model.common.model.SpBootProModel;
import com.example.restcontroller.SpBootProRestController;
import com.example.service.LoginService;
import com.example.util.TockenService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
	  @Autowired 
	  TockenService tockenService;
	  
	  
	  @Autowired
	  LoginService loginService;
	  
	  @Autowired
		private SpBootProRestController spBootProRestController;
	 
    @GetMapping("/after-varification-login")
    public String afterVarificationlogin() {
        return "login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "email-varification";
    }
    
   

    @GetMapping("/login-success")
    public String loginSuccess(HttpServletResponse response,
                               Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        String token = tockenService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getEmail()
        );

        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)
                .secure(true)          
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return "redirect:/contra/assignTasksp";
    }
    
    @GetMapping("registereduser")
	public String getRegisteredUserDetails(Model model) {
		
		List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails().stream().filter(a->a.getUserid()== 0).collect(Collectors.toList());
		
		model.addAttribute("spBootProModelList",spBootProModelList);
		return "registereduser";
	}
    
    @GetMapping("act")
	public String getHtmlReturn(Model model) {
		
		List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails();
		
		model.addAttribute("spBootProModelList",spBootProModelList);
		return "spbootpro";
	}
    
    @GetMapping("/forgot-password/{email}")
	public String forgotPassword( @PathVariable String email,  Model model) {

	    model.addAttribute("email", email);

	    return "forgot-password";

	}
    
    @GetMapping("/send-reset-password")
	public String sendLinkForgotPassword( @RequestParam("email") String email,  Model model) {

    	SimpleMailMessage sms= new SimpleMailMessage();
    	
    	String token = UUID.randomUUID().toString();

    	loginService.saveResetTocken(token, email);

        
	    return "redirect:/login?resetSent";

	}
    
    @GetMapping("/reset-password")
    public String resetPage(@RequestParam String token, Model model) {

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password) {

    	loginService.savePassword(token, password);

        return "redirect:/login?resetSuccess";
    }

}