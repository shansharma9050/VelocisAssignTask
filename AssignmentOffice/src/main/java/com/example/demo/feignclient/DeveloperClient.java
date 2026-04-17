package com.example.demo.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.FeignConfig;
import com.example.model.common.model.SpBootProModel;

@FeignClient(
	    name = "SPBOOTPRO",
	    configuration = FeignConfig.class
	)
	public interface DeveloperClient {

	    @GetMapping("/api/getDetail")
	    List<SpBootProModel> getDeveloperList();

	    @GetMapping("/api/getuserById")
	    SpBootProModel getUserById(@RequestParam("id") Long id);
	}
