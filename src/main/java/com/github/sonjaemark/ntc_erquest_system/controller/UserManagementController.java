package com.github.sonjaemark.ntc_erquest_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO; // Import this
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.user.UserManagementService;

@RestController
@RequestMapping("/api/user-management")
public class UserManagementController {

    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public ResponseEntity<List<RegisterResponseDTO>> getAllUser() {
        return ResponseEntity.ok(userManagementService.getAllUser());
    }

    @PostMapping("/toggle/{userId}")
    public ResponseEntity<RegisterResponseDTO> toggleActiveStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userManagementService.toggleActiveStatus(userId));
    }

    // New Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        userManagementService.setRegisterRequestDTO(registerRequestDTO);
        return ResponseEntity.ok(userManagementService.registerUser());
    }
}