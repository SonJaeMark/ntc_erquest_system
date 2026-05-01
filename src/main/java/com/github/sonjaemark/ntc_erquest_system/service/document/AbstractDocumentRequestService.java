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
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractDocumentRequestService extends AuthLevel{

    private DocumentRequestRequestDTO documentRequestDTO;
    protected final UserModelRepository userModelRepository;
    protected final DocumentRepository documentRepository;
    protected final AbstractRequestLogsService requestLogsService;

    // Constructor for sub-classes to pass the repository up
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

    public boolean logAction() {
        requestLogsService.logAction();
        return true;
    }

    // Inside AbstractDocumentRequestService
    public DocumentRequest mapToDocumentRequest(DocumentRequestRequestDTO documentRequestDTO2) {
        return DocumentRequest.builder()
            .purpose(documentRequestDTO2.purpose())
            .documentType(documentRequestDTO2.documentType())
            .additionalDetails(documentRequestDTO2.additionalDetails())
            .remarks(documentRequestDTO2.remarks())
            .status(documentRequestDTO2.status())
            .student(userModelRepository.findById(documentRequestDTO2.studentId()).orElse(null))
            .document(documentRepository.findById(documentRequestDTO2.documentId()).orElse(null)) 
            .build();
    }

    public DocumentRequestResponseDTO mapToDocumentResponseDTO(DocumentRequest savedDocumentRequest) {
        return new DocumentRequestResponseDTO(
            savedDocumentRequest.getId(),
            savedDocumentRequest.getPurpose(),
            savedDocumentRequest.getDocumentType(),
            savedDocumentRequest.getDocument() != null ? savedDocumentRequest.getDocument().getId() : null,
            savedDocumentRequest.getAdditionalDetails(),
            savedDocumentRequest.getRemarks(),
            savedDocumentRequest.getStatus(),
            savedDocumentRequest.getRequestedAt(),
            savedDocumentRequest.getUpdatedAt(),
            savedDocumentRequest.getStudent() != null ? savedDocumentRequest.getStudent().getId() : null,
            savedDocumentRequest.getStudent() != null ? 
                savedDocumentRequest.getStudent().getFirstName() + " " + savedDocumentRequest.getStudent().getLastName() : "Unknown",
            savedDocumentRequest.getRegistrar() != null ? savedDocumentRequest.getRegistrar().getId() : null
        );
    }



    public abstract DocumentRequestResponseDTO submit();
    public abstract DocumentRequestResponseDTO process();
    public abstract DocumentRequestResponseDTO accept();
}
