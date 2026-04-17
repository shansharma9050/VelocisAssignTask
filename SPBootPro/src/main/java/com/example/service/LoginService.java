package com.example.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.LoginModel;
import com.example.repository.LoginRepository;

@Service
public class LoginService {

	@Autowired
	LoginRepository loginRepository;
	@Autowired
	PasswordEncoder passwordEncoder; 
	
	 @Autowired
		private JavaMailSender javaMailSender;

	public LoginModel saveResetTocken(@RequestParam("resetTocken") String resetTocken,
			@RequestParam("email") String email) {

		SimpleMailMessage sms= new SimpleMailMessage();
    	
        String link =
                "http://localhost:8090/reset-password?token=" + resetTocken;
    	
    	sms.setSubject("Password Reset Link");
    	sms.setTo(email);
    	sms.setText("Click on this link for Reset Password : "+link);
    	
    	
    	javaMailSender.send(sms);
    	
		LoginModel lm = loginRepository.findByEmail(email);
		lm.setResettocken(resetTocken);
		lm.setResettockenexpiry(
	            LocalDateTime.now().plusMinutes(15)
	    );
		

		return loginRepository.save(lm);
	}

	public LoginModel savePassword(@RequestParam("resetTocken") String resetTocken,
			@RequestParam("password") String password) {

		LoginModel lm = loginRepository.findByResettocken(resetTocken);

		lm.setPassword(passwordEncoder.encode(password));
		lm.setResettocken(null);
		loginRepository.save(lm);

		return loginRepository.save(lm);
	}
	
}
