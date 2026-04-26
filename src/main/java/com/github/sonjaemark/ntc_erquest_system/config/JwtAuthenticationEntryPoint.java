package com.github.sonjaemark.ntc_erquest_system.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        LocalDateTime timestamp = LocalDateTime.now();

        String message = "Unauthorized user: " + authException.getMessage();

        String jsonResponse = String.format("""
            {
                "status": 401,
                "message": "%s",
                "timestamp": "%s"
            }
        """, message, timestamp);
        
        response.getWriter().write(jsonResponse);
        
    }
}
