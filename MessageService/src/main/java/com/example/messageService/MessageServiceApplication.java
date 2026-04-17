package com.example.messageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableDiscoveryClient  
@ComponentScan(basePackages = {"com.example",
        "com.example.messageService" })
public class MessageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}

}
