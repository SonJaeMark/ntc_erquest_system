package com.github.sonjaemark.ntc_erquest_system.controller;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.document.AbstractDocumentRequestService;
import com.github.sonjaemark.ntc_erquest_system.service.document.IDocumentRequestQueryService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.IRequestLogsQueryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/document-request")
public class DocumentRequestController {

    private final AbstractDocumentRequestService documentService;
    private final IDocumentRequestQueryService documentQueryService;
    private final IRequestLogsQueryService requestLogsQueryService;

    public DocumentRequestController(
        AbstractDocumentRequestService documentService, 
        IDocumentRequestQueryService documentQueryService,
        IRequestLogsQueryService requestLogsQueryService) {
        this.documentService = documentService;
        this.documentQueryService = documentQueryService;
        this.requestLogsQueryService =requestLogsQueryService;
    }

    @PostMapping("/submit")
    public ResponseEntity<DocumentRequestResponseDTO> submit(@RequestBody DocumentRequestRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.submit());
    }

    @PutMapping("/process")
    public ResponseEntity<DocumentRequestResponseDTO> process(@RequestBody DocumentRequestRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.process());
    }

    @PutMapping("/accept")
    public ResponseEntity<DocumentRequestResponseDTO> accept(@RequestBody DocumentRequestRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.accept());
    }

    @GetMapping("/student")
    public ResponseEntity<List<DocumentRequestResponseDTO>> getByStudent() {
        return ResponseEntity.ok(documentQueryService.getAllDocumentRequestByStudentId());
    }

    @GetMapping("/registrar")
    public ResponseEntity<List<DocumentRequestResponseDTO>> getByRegistrar() {
        return ResponseEntity.ok(documentQueryService.getAllAceptedRequestByRegistrarId());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DocumentRequestResponseDTO>> getUnaccepted() {
        return ResponseEntity.ok(documentQueryService.getAllUnacceptedRequest());
    }

    @GetMapping("/logs/{documentRequestId}")
    public ResponseEntity<List<RequestLogsResponseDTO>> getMethodName(@PathVariable Long documentRequestId) {
        return ResponseEntity.ok(requestLogsQueryService.getRequestLogsByDocumentRequestId(documentRequestId));
    }

}
