package com.example.demo.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entity.Users;
import com.example.demo.exceptions.InvalidSessionException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SessionService;
import java.util.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final UserRepository userRepository;

    public SessionAuthenticationFilter(SessionService sessionService,UserRepository userRepository) {
        this.sessionService = sessionService;
        this.userRepository=userRepository;
    }
    
    

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.equals("/users/register")
                || path.equals("/users/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String sessionId = request.getHeader("Session-Id");

        if (sessionId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session-Id header is missing");
            return;
        }

        try {

            Long userId = sessionService.getUserIdBySession(sessionId);


            		Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (InvalidSessionException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Session");
        }
    }
}