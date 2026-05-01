package com.github.sonjaemark.ntc_erquest_system.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.sonjaemark.ntc_erquest_system.exception.AccessTokenBlockedException;
import com.github.sonjaemark.ntc_erquest_system.service.user.CustomUserDetailsService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String path = request.getServletPath();
        String method = request.getMethod();

        if(header == null || !header.startsWith("Bearer ")) {
            if (!path.startsWith("/auth/")) {
                System.out.println("DEBUG: Missing or invalid Authorization header for " + method + " " + path);
            }
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        // Handle potential double "Bearer " prefix if it was mistakenly sent by frontend
        if (token.startsWith("Bearer ")) {
            System.out.println("DEBUG: Double Bearer prefix detected for " + path);
            token = token.substring(7);
        }

        if (jwtService.isTokenBlocked(token)) {
             System.out.println("DEBUG: Token is blocked for " + path);
             throw new AccessTokenBlockedException("Invalid Access Token, token are already blocked");
        }

        try {
            String username = jwtService.extractEmail(token);

            if(username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                var userDetails =
                        userDetailsService.loadUserByUsername(username);

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("DEBUG: Authenticated user " + username + " for " + method + " " + path);
            }
        } catch (JwtException | IllegalArgumentException ex) {
            System.out.println("DEBUG: JWT validation failed for " + path + ": " + ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}
