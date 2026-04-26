package com.github.sonjaemark.ntc_erquest_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.dto.CreateDocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.service.document.DocumentRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/student/document-requests")
public class DocumentRequestController {
    private final DocumentRequestService documentRequestService;

    public DocumentRequestController(DocumentRequestService documentRequestService) {
        this.documentRequestService = documentRequestService;
    }

    @PostMapping
    public ResponseEntity<DocumentRequestResponseDTO> submitRequest(@Valid @RequestBody CreateDocumentRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentRequestService.submitRequest(request));
    }

    @GetMapping("/document-types")
    public ResponseEntity<DocumentType[]> getDocumentTypes() {
        return ResponseEntity.ok(DocumentType.values());
    }
}
