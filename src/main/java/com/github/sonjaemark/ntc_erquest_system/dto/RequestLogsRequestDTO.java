package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

public record RequestLogsRequestDTO(
    Long documentRequestId,
    RequestStatus requestStatus,
    String remarks
) {}
