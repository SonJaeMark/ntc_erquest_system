package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;

public interface IDocumentRequestQueryService {
    List<DocumentRequestResponseDTO> getAllDocumentRequestByStudentId();
    List<DocumentRequestResponseDTO> getAllAceptedRequestByRegistrarId();
    List<DocumentRequestResponseDTO> getAllUnacceptedRequest();
}
