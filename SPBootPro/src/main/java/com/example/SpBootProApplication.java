package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import capchaFilter.CaptchaFilter;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {
	    "com.example.repository",
	    "com.example.model.repository"
	})
@EntityScan(basePackages = {
        "com.example.model.common.model",
        "com.example.model"
})
public class SpBootProApplication {

		@Bean
	    public CaptchaFilter captchaFilter() {
	        return new CaptchaFilter();
	    }
	
	public static void main(String[] args) {
		SpringApplication.run(SpBootProApplication.class, args);
	}

}
