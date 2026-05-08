package com.github.sonjaemark.ntc_erquest_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.dto.LoadAllUserResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.user.UserManagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-management")
public class UserManagementController {

    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userManagementService.register(request));
    }

    @PutMapping("/toggle-active/{userId}")
    public ResponseEntity<Boolean> toggleUserActiveStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userManagementService.toggleUserActiveStatus(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoadAllUserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }
}
