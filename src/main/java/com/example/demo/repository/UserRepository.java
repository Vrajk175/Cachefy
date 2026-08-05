package com.example.demo.repository;

import com.example.demo.entity.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    
    long countByRole(Role role);

}