package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;


import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;

import lombok.Data;

@Data
public abstract class AbstractDocumentRequestService {

    private DocumentRequestDTO documentRequestDTO;
    protected final UserModelRepository userModelRepository;
    protected final DocumentRepository documentRepository;

    // Constructor for sub-classes to pass the repository up
    protected AbstractDocumentRequestService(
        UserModelRepository userModelRepository,
        DocumentRepository documentRepository
    ) {
        this.userModelRepository = userModelRepository;
        this.documentRepository = documentRepository;
    }

    public boolean logAction(RequestStatus requestStatus) {
        // TODO: To be implemented on sprint two
        return true;
    }

    public boolean isAuthorized(List<UserRole> roles){
        // TODO: To be implemented on sprint two
        return true;
    }

    // Inside AbstractDocumentRequestService
    public DocumentRequest mapToDocumentRequest(DocumentRequestDTO dto) {
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

    public DocumentResponseDTO mapToDocumentResponseDTO(DocumentRequest entity) {
        return new DocumentResponseDTO(
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



    public abstract DocumentResponseDTO submit();
    public abstract DocumentResponseDTO process();
    public abstract DocumentResponseDTO accept();
}
