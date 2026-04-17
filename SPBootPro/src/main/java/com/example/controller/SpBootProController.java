package com.example.controller;
import org.springframework.web.client.RestTemplate;

import com.example.model.LoginModel;
import com.example.model.common.model.SpBootProModel;
import com.example.repository.LoginRepository;
import com.example.repository.SpBootProRepository;
import com.example.restcontroller.SpBootProRestController;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("contra")
public class SpBootProController {

	@Autowired
	private SpBootProRestController spBootProRestController;
	
	@Autowired
	private JavaMailSender javaMailSender;
	 
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SpBootProRepository spBootProRepository;
	
	@Autowired
	String apiUrl;
	
	@GetMapping("/assignTasksp")
	public String redirectToTaskService() {

		System.out.print("SpBootProController===================>");
	    
	    return "redirect:"+apiUrl+"/taskassignment/myProfile";
	    
	}
	
	@GetMapping("userDetails")
	public List<SpBootProModel> getUserDetails(Model model) {
		
		List<SpBootProModel> spBootProModelList=spBootProRestController.getDetails();
		
		model.addAttribute("spBootProModelList",spBootProModelList);
		return spBootProModelList;
	}
	
	@PostMapping("register")
	public String setUserDetails(
	        @ModelAttribute("spBootProModel") SpBootProModel spBootProModel,
	        Model model) {

	    spBootProRestController.saveData(spBootProModel);

	    model.addAttribute("message", "Registration successful");

	    return "success";
	}
	
	
	@GetMapping("userDetails/{id}")
	public String userDetails(@PathVariable Long id, Model model) {

	    SpBootProModel user = spBootProRestController.getUserById(id);

	    model.addAttribute("user", user);

	    return "userDetails";

	}
	
	private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&!";

    private static final int PASSWORD_LENGTH = 10;

    public static String generatePassword() {

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {

            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));

        }

        return password.toString();
    }
	
	@PostMapping("userAction")
	public String userAction(@RequestParam Long id,
							 @RequestParam String email,
	                         @RequestParam String action,
	                         @RequestParam String remark) {
		
		SimpleMailMessage sms= new SimpleMailMessage();

		 LoginModel loginModel = loginRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
		SpBootProModel smodel=spBootProRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
	    if(action.equals("approve")){
	    	smodel.setUserid(id);
	    	smodel.setStatus(action);
	    	smodel.setRemark(remark);
	    	
           
            loginModel.setPassword(passwordEncoder.encode(generatePassword()));
	
	    	
	    	sms.setSubject("Application For Registration");
	    	sms.setTo(email);
	    	sms.setText("Your application for registration has been approved and your Password for Login is : [" + generatePassword()+"]" );
	    	
	    }
	    else if(action.equals("reject")){
	    	smodel.setRemark(remark);
	    	smodel.setStatus(action);
	    	
	    	sms.setSubject("Application For Registration");
	    	sms.setTo(email);
	    	sms.setText("Your application for registration has been rejected , Please try again with correct information");
	    }
	    loginRepository.save(loginModel);
	    spBootProRepository.save(smodel);
	    
	    javaMailSender.send(sms);
	    return "redirect:/contra/registereduser";
	}
}
