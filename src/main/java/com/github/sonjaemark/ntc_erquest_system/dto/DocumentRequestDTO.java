package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
public record DocumentRequestDTO(
    DocumentType documentType,
    String documentContent, // Typically a Base64 encoded string or a URL
    Long studentId          // ID of the student who owns this document
) {}
