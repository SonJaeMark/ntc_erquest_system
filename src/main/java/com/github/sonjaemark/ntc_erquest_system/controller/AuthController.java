package com.github.sonjaemark.ntc_erquest_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.dto.AuthResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.LoginRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.LogoutRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RefreshTokenDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO token) {
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(authService.refreshToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> logout(@Valid @RequestBody LogoutRequestDTO token) {
        return ResponseEntity.ok(authService.logout(token));
    }
}
