package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.model.common.model.SpBootProModel;
import com.example.model.repository.SpBootProRepository;
import com.example.service.EmailVarificationService;

@Controller
@RequestMapping("/auth")
public class OTPController {

	 @Autowired
	    SpBootProRepository spBootProRepository;

	
    @Autowired
    private EmailVarificationService emailVarificationService;

    @PostMapping("/send-otp")
    @ResponseBody
    public String sendOtp(@RequestParam("email") String email) {
SpBootProModel sp=spBootProRepository.findByEmail(email).orElse(null);
    	
    	if(sp !=null && "approve".equals(sp.getStatus())) {
    		 emailVarificationService.generateAndSendOtp(email);
    	}
        return "OTP sent successfully to " + email;
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp,@RequestParam("username") String username, Model model) {
        boolean isValid = emailVarificationService.verifyOtp(email, otp);

        if (isValid) {
            model.addAttribute("message", "OTP Verified Successfully! Please proceed to login.");
            
            System.out.print("============================>"+username);
            model.addAttribute("username",username);
            model.addAttribute("email",email);
            return "login"; 
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "verifyEmail"; 
        }
    }

    @GetMapping("/verify")
    public String showVerificationPage() {
        return "verifyEmail";
    }
}
