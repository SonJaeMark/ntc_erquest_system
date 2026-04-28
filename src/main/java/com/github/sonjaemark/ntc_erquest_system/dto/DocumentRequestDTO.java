package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentRequestDTO(

        @NotNull(message = "Document type is required") DocumentType documentType,

        @NotBlank(message = "Document content is required") String documentContent,

        @NotNull(message = "Student ID is required") Long studentId

) {
}