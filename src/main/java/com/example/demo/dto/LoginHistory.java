package com.example.demo.dto;

import java.io.Serializable;

public class LoginHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String loginTime;

    public LoginHistory() {
    }

    public LoginHistory(String email, String loginTime) {
        this.email = email;
        this.loginTime = loginTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}