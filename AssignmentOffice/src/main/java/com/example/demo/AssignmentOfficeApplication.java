package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
        "com.example.model",
        "com.example.demo",
        "com.example"
})
@EnableJpaRepositories(basePackages = {
        "com.example.model.repository",
        "com.example.demo.repository"
})
@EntityScan(basePackages = {
        "com.example.model.common.model",
        "com.example.demo.assignmentofice.model"
})
public class AssignmentOfficeApplication {

	@Value("${api.url}")
	String apiUrl;
	
	@Bean
	String apiUrl() {
		return apiUrl;
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate(); 
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AssignmentOfficeApplication.class, args);
	}
    
}
