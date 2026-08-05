package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.dto.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userservice;
	
	public UserController(UserService userservice) {
		this.userservice=userservice;
	}
	
	
	@PostMapping("/register")
	public RegisterResponse RegisterUser(@Valid @RequestBody RegisterRequest register) {
		
		userservice.Register(register);
		
		return new RegisterResponse("user register successs");
		
	}
	
	@PostMapping("/login")
	public LoginResponse LoginUser(@Valid @RequestBody LoginRequest login) {
		
		return userservice.Login(login);
		
		
	}
	
	@GetMapping("/profile")
	public ProfileResponse getProfile() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Long userId = (Long) authentication.getPrincipal();
		
		return userservice.getProfile(userId);
	}
	
	@PostMapping("/logout")
	public LogoutResponse logout(@RequestHeader("session-id") String sessionId) {
		userservice.logout(sessionId);
		
		return new LogoutResponse("logout succesfull");
	}
	
	@PutMapping("/profile")
	public RegisterResponse updprofile(@Valid @RequestBody UpdateProfileRequest request) {
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Long userId = (Long) authentication.getPrincipal();
		
		userservice.UpdateProfile(userId,request);
		
		return new RegisterResponse("profile updated succesfully");
		
	}

}
