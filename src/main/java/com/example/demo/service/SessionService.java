package com.example.demo.service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.InvalidSessionException;


@Service
public class SessionService {

	private final RedisTemplate<String, String> redisTemplate;
    
    public SessionService(RedisTemplate<String,String> redisTemplate) {
    	this.redisTemplate = redisTemplate;
    }

    private static final long SESSION_TIMEOUT = 30;

    public String createSession(Long userId) {

        String sessionId = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                sessionId,
                userId.toString(),
                Duration.ofMinutes(SESSION_TIMEOUT)
        );

        return sessionId;
    }
    
    public Long getUserIdBySession(String sessionId) {
    	
    	String userId = redisTemplate.opsForValue().get(sessionId);
    	
    	if(userId == null) {
    		throw new InvalidSessionException("invalid session");
    	}
    	
    	return Long.parseLong(userId);
    	
    }
    public String deleteSession(String sesssionId) { 
    	return redisTemplate.opsForValue().getAndDelete(sesssionId); 
    	}
}