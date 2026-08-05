package com.example.demo.dto;

public class LoginResponse {
	
	private String message;
	
	private String sessionId;
	
	

	public LoginResponse(String message , String sessionId) {
		this.message = message;
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
	

}
