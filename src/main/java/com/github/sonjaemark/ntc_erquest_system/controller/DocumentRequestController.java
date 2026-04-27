package com.github.sonjaemark.ntc_erquest_system.controller;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.document.ConcreteDocumentRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-request")
public class DocumentRequestController {

    private final ConcreteDocumentRequestService documentService;

    public DocumentRequestController(ConcreteDocumentRequestService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<DocumentResponseDTO> submit(@RequestBody DocumentRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.submit());
    }

    @PutMapping("/process")
    public ResponseEntity<DocumentResponseDTO> process(@RequestBody DocumentRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.process());
    }

    @PutMapping("/accept")
    public ResponseEntity<DocumentResponseDTO> accept(@RequestBody DocumentRequestDTO dto) {
        documentService.setDocumentRequestDTO(dto);
        return ResponseEntity.ok(documentService.accept());
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<DocumentResponseDTO>> getByStudent(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getAllByStudentId(id));
    }

    @GetMapping("/registrar/{id}")
    public ResponseEntity<List<DocumentResponseDTO>> getByRegistrar(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getAllByRegistrarId(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DocumentResponseDTO>> getUnaccepted() {
        return ResponseEntity.ok(documentService.getAllUnacceptedRequest());
    }
}
