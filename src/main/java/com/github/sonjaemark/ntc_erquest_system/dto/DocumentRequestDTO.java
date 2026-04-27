package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

public record DocumentRequestDTO(
    Purpose purpose,
    DocumentType documentType, // Added
    Long documentId,           // Added to link the specific Document entity
    String additionalDetails,
    String remarks,
    RequestStatus status,
    Long studentId,
    Long registrarId
) {}
