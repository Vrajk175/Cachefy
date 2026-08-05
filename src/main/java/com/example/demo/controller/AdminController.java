package com.example.demo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AdminStatsResponse;
import com.example.demo.entity.Users;
import com.example.demo.service.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "Session-Id")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminStatsResponse dashboard() {
        return adminService.dashboard();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Users getUserById(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @GetMapping("/users/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public Users getUserByEmail(@PathVariable String email) {
        return adminService.getUserByEmail(email);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id);
    }
}