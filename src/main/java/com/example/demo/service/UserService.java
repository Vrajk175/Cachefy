package com.example.demo.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.Users;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder, SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.sessionService=sessionService;
    }
    
    
    public void register(RegisterRequest request) {

      

        var user = userRepository.findByEmail(request.getEmail());

    

        if (user.isPresent()) {
            System.out.println("Duplicate email found");
            throw new EmailAlreadyExistsException("Email already Exists");
        }

        Users users = new Users();

        users.setEmail(request.getEmail());
        users.setName(request.getName());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setRole(Role.USER);

        userRepository.save(users);
    }
    public LoginResponse login(LoginRequest request) {
    	Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException("Invalid password");
        }
        String sessionId = sessionService.createSession(user.getId());
        return new LoginResponse("Login successfull", sessionId);

    }
    @Cacheable(value = "users" , key = "#userId")
    public ProfileResponse getProfile(Long userId) {
    	
    
    	
    	Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    	return new ProfileResponse(user.getId(),user.getName(),user.getEmail());

    	
    }
    public void logout(String sessionId) {
    	
    	sessionService.deleteSession(sessionId);
    }
    @CacheEvict(value = "users", key = "#userId")
    public void updateProfile(Long userId, UpdateProfileRequest request) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        user.setName(request.getName());

        userRepository.save(user);
    }
    @CacheEvict(value = "users", key = "#userId")
    public void changePassword(Long userId, ChangePasswordRequest request) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException("Old password is incorrect");
        }
        

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
    @CacheEvict(value = "users", key = "#userId")
    public void deleteAccount(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }

}