package com.github.sonjaemark.ntc_erquest_system.service.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.model.JwtBlockList;
import com.github.sonjaemark.ntc_erquest_system.repository.JwtBlockListRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET = "super-secret-key-super-secret-key-super-secret";
    private final JwtBlockListRepository jwtBlockListRepository;

    JwtService(JwtBlockListRepository jwtBlockListRepository){
        this.jwtBlockListRepository = jwtBlockListRepository;
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Long userId, String email) {

        return Jwts.builder()
        .claim("userId", userId)
        .subject(email)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
        .signWith(getKey())
        .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Long extractUserId(String token) {

        Object userId = Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId");

        return Long.valueOf(userId.toString());
    }

    public boolean isTokenBlocked(String accessToken){
        return jwtBlockListRepository.findByAccessToken(accessToken).isPresent();
    }

    public void blockAccessToken(String token){
        jwtBlockListRepository.save(
            JwtBlockList
                .builder()
                .accessToken(token)
                .blockDateTime(LocalDateTime.now())
            .build()
        );

    }
}
