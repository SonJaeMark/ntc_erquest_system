package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

public record AuthResponseDTO(
    String accessToken,
    String refreshToken,
    Long userId,
    String email,
    UserRole role,
    boolean isActive
) {

}