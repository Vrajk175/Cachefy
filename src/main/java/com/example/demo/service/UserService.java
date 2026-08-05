package com.example.demo.service;

import org.apache.catalina.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.config.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.*;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.repository.UserRepo;

@Service
public class UserService {
	
	private final UserRepo userrepo;
	
	private final PasswordEncoder passwordencode;
	
	private final SessionService sessionService;
	
	public UserService(UserRepo userrepo,PasswordEncoder passwordencode , SessionService sessionService) {
		
		this.userrepo=userrepo;
		this.passwordencode = passwordencode;
		this.sessionService = sessionService;
		
	}
	
	public String Greet() {
		return "hello world";
	}
	
	public void Register(RegisterRequest register) {
		
		Users user = new Users();
		
		user.setName(register.getName());
		user.setEmail(register.getEmail());
		user.setPassword(passwordencode.encode(register.getPassword()));
		
		if(userrepo.findByEmail(register.getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException("Email already exixts");
		}
		

		userrepo.save(user);
		
		
	}
	
	public LoginResponse Login(LoginRequest request) {

	    Users user = userrepo.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if (!passwordencode.matches(
	            request.getPassword(),
	            user.getPassword())) {

	        throw new InvalidPasswordException("Invalid password");
	    }
	    String sessionId = sessionService.createSession(user.getId());

	    return new LoginResponse("login successfull",sessionId);
	}
	@Cacheable(value = "users" ,key = "#userId")
	public ProfileResponse getProfile(Long userId) {
		
		
		Users user = userrepo.findById(userId)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));
		
		return new ProfileResponse(user.getId(),user.getEmail(),user.getName());
			
	}
	
	public void logout(String sessionId) {

	    sessionService.deleteSession(sessionId);

	}
	@CacheEvict(value = "users" ,key = "#userId")
	public void UpdateProfile(Long userId,UpdateProfileRequest request) {
		
		Users user = userrepo.findById(userId)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));
		
		user.setName(request.getName());
		
		userrepo.save(user);
		
		
	}
	
	

}
