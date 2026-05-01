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

    private static final String AUTH_PATH_PREFIX = "/auth/";
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return  (path.startsWith(AUTH_PATH_PREFIX) && path.contains("login")) ||
                (path.startsWith(AUTH_PATH_PREFIX) && path.contains("refresh-token")) ||  
                (path.startsWith(AUTH_PATH_PREFIX) && path.contains("logout"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String path = request.getServletPath();

        if(header == null || !header.startsWith("Bearer ")) {
            System.out.println("DEBUG: Missing or invalid Authorization header for path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        // Handle potential double "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            System.out.println("DEBUG: Double Bearer prefix detected, stripping it.");
            token = token.substring(7);
        }
        System.out.println("DEBUG: Token received for path: " + path);

        if (jwtService.isTokenBlocked(token)) {
            System.out.println("DEBUG: Token is blocked");
            throw new AccessTokenBlockedException("Invalid Access Token, token are already blocked");
        }

        try {
            String username = jwtService.extractEmail(token);
            System.out.println("DEBUG: Extracted username: " + username);

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
                System.out.println("DEBUG: Authentication set in SecurityContext for user: " + username);
            }
        } catch (JwtException | IllegalArgumentException ex) {
            System.out.println("DEBUG: JWT extraction failed: " + ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}
