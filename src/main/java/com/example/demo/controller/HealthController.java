package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

@RestController
public class HealthController {
	
	private final UserService service;
	
	public HealthController(UserService service) {
		
		this.service=service;
		
	}
	
	
	@GetMapping("/health")
	public String health(){
		return "application";
	}
	
	@GetMapping("/greet")
	public String greeting() {
		return service.Greet();
	}
}
