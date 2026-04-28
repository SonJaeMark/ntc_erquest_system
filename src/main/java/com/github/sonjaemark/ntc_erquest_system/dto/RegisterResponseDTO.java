package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

public record RegisterResponseDTO(
    Long userId,
    String email,
    UserRole role
    
) {}
