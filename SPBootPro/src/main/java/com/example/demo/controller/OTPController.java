package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.EmailVarificationService;

@Controller
@RequestMapping("/auth")
public class OTPController {

    @Autowired
    private EmailVarificationService emailVarificationService;

    @PostMapping("/send-otp")
    @ResponseBody
    public String sendOtp(@RequestParam("email") String email) {
        emailVarificationService.generateAndSendOtp(email);
        return "OTP sent successfully to " + email;
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        boolean isValid = emailVarificationService.verifyOtp(email, otp);

        if (isValid) {
            model.addAttribute("message", "OTP Verified Successfully! Please proceed to login.");
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
