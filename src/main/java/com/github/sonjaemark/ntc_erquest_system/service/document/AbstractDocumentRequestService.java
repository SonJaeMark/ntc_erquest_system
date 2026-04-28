package com.github.sonjaemark.ntc_erquest_system.service.document;



import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
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
    public DocumentRequest mapToDocumentRequest(DocumentRequestRequestDTO dto) {
        return DocumentRequest.builder()
            .purpose(dto.purpose())
            .documentType(dto.documentType())
            .additionalDetails(dto.additionalDetails())
            .remarks(dto.remarks())
            .status(dto.status())
            .student(userModelRepository.findById(dto.studentId()).orElse(null))
            .document(documentRepository.findById(dto.documentId()).orElse(null)) 
            .build();
    }

    public DocumentRequestResponseDTO mapToDocumentResponseDTO(DocumentRequest entity) {
        return new DocumentRequestResponseDTO(
            entity.getId(),
            entity.getPurpose(),
            entity.getDocumentType(),
            entity.getDocument() != null ? entity.getDocument().getId() : null,
            entity.getAdditionalDetails(),
            entity.getRemarks(),
            entity.getStatus(),
            entity.getRequestedAt(),
            entity.getUpdatedAt(),
            entity.getStudent() != null ? entity.getStudent().getId() : null,
            entity.getStudent() != null ? 
                entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName() : "Unknown",
            entity.getRegistrar() != null ? entity.getRegistrar().getId() : null
        );
    }



    public abstract DocumentRequestResponseDTO submit();
    public abstract DocumentRequestResponseDTO process();
    public abstract DocumentRequestResponseDTO accept();
}
