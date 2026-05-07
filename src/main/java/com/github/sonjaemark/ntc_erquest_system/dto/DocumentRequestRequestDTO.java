package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentRequestRequestDTO(
    Long id,
    @NotNull(message = "Purpose is required")
    Purpose purpose,
    @NotNull(message = "Document type is required")
    DocumentType documentType,
    @NotNull(message = "Document ID is required")
    Long documentId,
    @Size(max = 500, message = "Additional details must not exceed 500 characters")
    String additionalDetails,
    @Size(max = 255, message = "Remarks must not exceed 255 characters")
    String remarks,
    @NotNull(message = "Status is required")
    RequestStatus status,
    @NotNull(message = "Student ID is required")
    Long studentId,
    @NotNull(message = "Registrar ID is required")
    Long registrarId
) {}
