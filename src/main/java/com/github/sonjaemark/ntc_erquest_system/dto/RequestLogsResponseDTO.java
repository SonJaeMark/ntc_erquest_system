package com.github.sonjaemark.ntc_erquest_system.dto;

import java.time.LocalDateTime;

import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

public record RequestLogsResponseDTO(
    Long id,
    RequestStatus requestStatus,
    LocalDateTime dateAction,
    String remarks
) {}
