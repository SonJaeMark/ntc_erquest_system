package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

public record LoadAllUserResponseDTO(
    Long userId,
    String email,
    UserRole role,
    String fullName,
    boolean isActive
) {

}
