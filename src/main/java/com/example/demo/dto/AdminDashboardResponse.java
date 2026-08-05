package com.example.demo.dto;

public class AdminDashboardResponse {

    private long totalUsers;

    public AdminDashboardResponse(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
}