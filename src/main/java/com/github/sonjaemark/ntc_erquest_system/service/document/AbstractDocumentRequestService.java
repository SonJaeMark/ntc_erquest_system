package com.github.sonjaemark.ntc_erquest_system.service.document;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.AbstractRequestLogsService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractDocumentRequestService extends AuthLevel {

    private DocumentRequestRequestDTO documentRequestDTO;

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

    protected boolean logAction() {
        requestLogsService.logAction();
        return true;
    }

    protected DocumentRequest mapToDocumentRequest(DocumentRequestRequestDTO documentRequestDTO) {
        return DocumentRequest.builder()
                .purpose(documentRequestDTO.purpose())
                .documentType(documentRequestDTO.documentType())
                .additionalDetails(documentRequestDTO.additionalDetails())
                .remarks(documentRequestDTO.remarks())
                .status(documentRequestDTO.status())
                .student(userModelRepository.findById(documentRequestDTO.studentId()).orElse(null))
                .document(documentRepository.findById(documentRequestDTO.documentId()).orElse(null))
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

    public abstract DocumentRequestResponseDTO submit();

    public abstract DocumentRequestResponseDTO process();

    public abstract DocumentRequestResponseDTO accept();
}