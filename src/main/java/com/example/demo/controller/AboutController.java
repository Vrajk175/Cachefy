package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {

    @GetMapping("/about")
    public Map<String,String> about(){

        Map<String,String> map=new HashMap<>();

        map.put("Project","CacheFlow");
        map.put("Backend","Spring Boot");
        map.put("Database","PostgreSQL");
        map.put("Cache","Redis");
        map.put("Security","Spring Security");
        map.put("Developer","Your Name");

        return map;
    }
}