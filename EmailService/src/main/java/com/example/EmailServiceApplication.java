package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EmailServiceApplication {

	@Value("${api.url}")
	String apiUrl;
	
	@Bean
	String apiUrl() {
		return apiUrl;
	} 
	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

}
