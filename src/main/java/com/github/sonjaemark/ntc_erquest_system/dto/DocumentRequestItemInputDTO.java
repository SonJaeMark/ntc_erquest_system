package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DocumentRequestItemInputDTO(
        @NotNull(message = "Document type is required")
        DocumentType documentType,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Min(value = 1, message = "Copies must be at least 1")
        int copies
) {
}