package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LogoutResponse;
import com.example.demo.dto.ProfileResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.RegisterResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {

        userService.register(request);

        return new RegisterResponse("User Registered Successfully");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        return userService.login(request);
    }

    @SecurityRequirement(name = "Session-Id")
    @GetMapping("/me")
    public ProfileResponse me() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        return userService.getProfile(userId);
    }

    @SecurityRequirement(name = "Session-Id")
    @PostMapping("/logout")
    public LogoutResponse logout(
            @RequestHeader("Session-Id") String sessionId) {

        userService.logout(sessionId);

        return new LogoutResponse("Logout Successfully");
    }

    @SecurityRequirement(name = "Session-Id")
    @PutMapping("/profile")
    public RegisterResponse updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        userService.updateProfile(userId, request);

        return new RegisterResponse("Profile Updated Successfully");
    }

    @SecurityRequirement(name = "Session-Id")
    @PutMapping("/password")
    public RegisterResponse changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        userService.changePassword(userId, request);

        return new RegisterResponse("Password Changed Successfully");
    }

    @SecurityRequirement(name = "Session-Id")
    @DeleteMapping("/account")
    public RegisterResponse deleteAccount() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        userService.deleteAccount(userId);

        return new RegisterResponse("Account Deleted Successfully");
    }
}