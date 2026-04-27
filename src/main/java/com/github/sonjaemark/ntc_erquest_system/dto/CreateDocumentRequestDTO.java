package com.github.sonjaemark.ntc_erquest_system.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateDocumentRequestDTO(
        @NotBlank(message = "Purpose is required")
        @Size(max = 150, message = "Purpose must not exceed 150 characters")
        String purpose,

        @Size(max = 500, message = "Remarks must not exceed 500 characters")
        String remarks,

        @NotEmpty(message = "At least one requested document is required")
        List<@Valid DocumentRequestItemInputDTO> items
) {
}