package com.github.sonjaemark.ntc_erquest_system.service.document;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.DocumentNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.exception.IdNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.AbstractRequestLogsService;

public abstract class AbstractDocumentRequestService extends AuthLevel {

    protected final UserModelRepository userModelRepository;
    protected final DocumentRepository documentRepository;
    protected final AbstractRequestLogsService requestLogsService;

    protected AbstractDocumentRequestService(
            UserModelRepository userModelRepository,
            DocumentRepository documentRepository,
            AuthService authService,
            AbstractRequestLogsService requestLogsService
    ) {
        super(authService);
        this.userModelRepository = userModelRepository;
        this.documentRepository = documentRepository;
        this.requestLogsService = requestLogsService;
    }

    protected boolean logAction(DocumentRequest documentRequest, String remarks, RequestStatus requestStatus) {
        requestLogsService.logAction(documentRequest, remarks, requestStatus);
        return true;
    }

    protected DocumentRequest mapToDocumentRequest(DocumentRequestRequestDTO documentRequestDTO) {
        UserModel student = userModelRepository.findById(documentRequestDTO.studentId())
                .orElseThrow(() -> new IdNotFoundException("Student findByEmail with ID: " + documentRequestDTO.studentId()));

        Document document = documentRepository.findById(documentRequestDTO.documentId())
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + documentRequestDTO.documentId()));

        UserModel registrar = null;
        if (documentRequestDTO.registrarId() != null) {
            registrar = userModelRepository.findById(documentRequestDTO.registrarId())
                    .orElseThrow(() -> new IdNotFoundException("Registrar not found with ID: " + documentRequestDTO.registrarId()));
        }

        return DocumentRequest.builder()
                .id(documentRequestDTO.id())
                .purpose(documentRequestDTO.purpose())
                .documentType(documentRequestDTO.documentType())
                .additionalDetails(documentRequestDTO.additionalDetails())
                .remarks(documentRequestDTO.remarks())
                .status(documentRequestDTO.status())
                .student(student)
                .document(document)
                .registrar(registrar)
                .build();
    }

    protected DocumentRequestResponseDTO mapToDocumentResponseDTO(DocumentRequest documentRequest) {
        return new DocumentRequestResponseDTO(
                documentRequest.getId(),
                documentRequest.getPurpose(),
                documentRequest.getDocumentType(),
                documentRequest.getDocument() != null ? documentRequest.getDocument().getId() : null,
                documentRequest.getAdditionalDetails(),
                documentRequest.getRemarks(),
                documentRequest.getStatus(),
                documentRequest.getRequestedAt(),
                documentRequest.getUpdatedAt(),
                documentRequest.getStudent() != null ? documentRequest.getStudent().getId() : null,
                documentRequest.getStudent() != null
                        ? documentRequest.getStudent().getFirstName() + " " + documentRequest.getStudent().getLastName()
                        : "Unknown",
                documentRequest.getRegistrar() != null ? documentRequest.getRegistrar().getId() : null
        );
    }

    public abstract DocumentRequestResponseDTO submit(DocumentRequestRequestDTO dto);

    public abstract DocumentRequestResponseDTO process(DocumentRequestRequestDTO dto);

    public abstract DocumentRequestResponseDTO accept(DocumentRequestRequestDTO dto);

    public abstract DocumentRequestResponseDTO cancelRequest(Long documentRequestId);
}
