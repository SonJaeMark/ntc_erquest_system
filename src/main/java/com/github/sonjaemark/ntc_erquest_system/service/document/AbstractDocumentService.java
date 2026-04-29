package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractDocumentService extends AuthLevel{

    private DocumentRequestDTO documentRequestDTO;
    private final UserModelRepository userModelRepository;

    protected AbstractDocumentService(AuthService authService, UserModelRepository userModelRepository){
        super(authService);
        this.userModelRepository = userModelRepository;
    }

    public Document mapToDocument(DocumentRequestDTO dto) {
        return Document.builder()
            .documentType(dto.documentType())
            .documentContent(dto.documentContent())
            // Note: You must fetch the student from your UserRepository before this 
            // or pass it in as a parameter to satisfy the ManyToOne relationship.
            .student(userModelRepository.findById(dto.studentId()).orElse(null))
            .build();
    }

    public DocumentResponseDTO mapToDocumentResponseDTO(Document entity) {
        return new DocumentResponseDTO(
            entity.getId(),
            entity.getUploadedAt(),
            entity.getDocumentType(),
            entity.getDocumentContent(),
            entity.getStudent() != null ? entity.getStudent().getId() : null,
            entity.getStudent() != null ? 
                entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName() : "Unknown"
        );
    }


    public abstract DocumentResponseDTO save();
    public abstract List<DocumentResponseDTO> getStudentsDocuments();

}
