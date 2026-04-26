package com.github.sonjaemark.ntc_erquest_system.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ExceptionResponseDTO(
    HttpStatus status,
    String message,
    LocalDateTime timestamp
) {}
