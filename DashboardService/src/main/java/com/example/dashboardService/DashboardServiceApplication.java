package com.example.dashboardService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.example",
                               "com.example.dashboardService",
                               "com.example.messageService" })
public class DashboardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardServiceApplication.class, args);
	}

}
