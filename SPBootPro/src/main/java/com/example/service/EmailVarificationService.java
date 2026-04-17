package com.example.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVarificationService {

    private Map<String, String> otpStore = new ConcurrentHashMap<>();

    @Autowired
    private JavaMailSender javaMailSender;

    public void generateAndSendOtp(String mail) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpStore.put(mail, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }

    public boolean verifyOtp(String email, String OTP) {
        String storedOtp = otpStore.get(email);
        return storedOtp != null && storedOtp.equals(OTP);
    }
}
