package com.example.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {

    private static final String SECRET_KEY = "6LeHwo8sAAAAAD59PUxONzlx2loP-8Rr6HZ1dz3q";

    public boolean verifyCaptcha(String captchaResponse) {

        if (captchaResponse == null || captchaResponse.isEmpty()) {
            return false;
        }

        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response = restTemplate.postForObject(
                url + "?secret=" + SECRET_KEY + "&response=" + captchaResponse,
                null,
                Map.class
        );

        System.out.println("Captcha API Response: " + response);

        if (response == null) {
            return false;
        }

        Object success = response.get("success");

        return success instanceof Boolean && (Boolean) success;
    }
}
