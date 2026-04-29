package com.github.sonjaemark.ntc_erquest_system.dto;

import java.time.LocalDateTime;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;

public record DocumentResponseDTO(
    Long id,
    LocalDateTime uploadedAt,
    DocumentType documentType,
    String documentContent,
    Long studentId,
    String studentFullName // Flattened for easy UI display
) {}
