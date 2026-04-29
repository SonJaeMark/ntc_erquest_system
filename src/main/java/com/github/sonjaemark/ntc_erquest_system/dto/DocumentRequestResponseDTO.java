package com.github.sonjaemark.ntc_erquest_system.dto;

import java.time.LocalDateTime;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

public record DocumentRequestResponseDTO(
    Long id,
    Purpose purpose,
    DocumentType documentType, // Added
    Long documentId,           // Added
    String additionalDetails,
    String remarks,
    RequestStatus status,
    LocalDateTime requestedAt,
    LocalDateTime updatedAt,
    Long studentId,
    String studentFullName,
    Long registrarId
) {}
