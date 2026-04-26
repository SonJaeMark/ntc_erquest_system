package com.github.sonjaemark.ntc_erquest_system.service.document;

import com.github.sonjaemark.ntc_erquest_system.dto.CreateDocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;

public interface DocumentRequestService {
    DocumentRequestResponseDTO submitRequest(CreateDocumentRequestDTO request);
}