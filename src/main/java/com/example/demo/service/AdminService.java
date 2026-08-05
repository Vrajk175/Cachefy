package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AdminStatsResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.Users;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Dashboard Statistics
    public AdminStatsResponse dashboard() {

        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.countByRole(Role.ADMIN);
        long totalNormalUsers = userRepository.countByRole(Role.USER);

        return new AdminStatsResponse(
                totalUsers,
                totalAdmins,
                totalNormalUsers);
    }

    // Get All Users
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User By ID
    public Users getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    // Get User By Email
    public Users getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    // Delete User
    public String deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        userRepository.delete(user);

        return "User Deleted Successfully";
    }

}