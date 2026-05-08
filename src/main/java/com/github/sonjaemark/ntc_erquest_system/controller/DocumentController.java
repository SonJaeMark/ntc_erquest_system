package com.github.sonjaemark.ntc_erquest_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.document.AbstractDocumentService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final AbstractDocumentService documentService;

    public DocumentController(AbstractDocumentService documentService){
        this.documentService = documentService;
    }

    @GetMapping("/student")
    public ResponseEntity<List<DocumentResponseDTO>> getStudentsDocuments(){
        return ResponseEntity.ok(documentService.getStudentsDocuments());
    }

    @PostMapping("/save")
    public ResponseEntity<DocumentResponseDTO> saveStudentDocument(@RequestBody DocumentRequestDTO dto){
        return ResponseEntity.ok(documentService.save(dto));
    }
}
