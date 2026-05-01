package com.github.sonjaemark.ntc_erquest_system.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.service.export.ExportService;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/document-requests")
    public ResponseEntity<byte[]> documentRequests() {
        return csv(exportService.exportDocumentRequestsToCsv(), "document_requests.csv");
    }

    @GetMapping("/payments")
    public ResponseEntity<byte[]> payments() {
        return csv(exportService.exportPaymentsToCsv(), "payments.csv");
    }

    private ResponseEntity<byte[]> csv(byte[] data, String filename) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .body(data);
    }
}
