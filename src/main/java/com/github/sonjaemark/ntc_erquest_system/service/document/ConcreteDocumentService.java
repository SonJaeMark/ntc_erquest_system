package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@Service
@Transactional
public class ConcreteDocumentService extends AbstractDocumentService{

    private final DocumentRepository documentRepository;

    protected ConcreteDocumentService(AuthService authService, 
            UserModelRepository userModelRepository,
        DocumentRepository documentRepository) {
        super(authService, userModelRepository);
        this.documentRepository = documentRepository;
    }

    @Override
    public DocumentResponseDTO save(DocumentRequestDTO documentRequestDTO) {
        isAuthorized(List.of(UserRole.ADMIN));
        
        documentRepository.findByStudentIdAndDocumentType(documentRequestDTO.studentId(), documentRequestDTO.documentType())
            .ifPresent(doc -> {
                throw new IllegalArgumentException("Document type already exists for this student");
            });
        
        Document document = Document.builder()
                .documentType(documentRequestDTO.documentType())
                .student(userModelRepository.findById(documentRequestDTO.studentId()).orElseThrow(() -> new IllegalArgumentException("Student not found")))
                .documentContent(documentRequestDTO.documentContent())
                .build();
        document = documentRepository.save(document);
        return mapToDocumentResponseDTO(document);
    }

    @Override
    public List<DocumentResponseDTO> getStudentsDocuments() {
        Long studentId = isAuthorized(List.of(UserRole.STUDENT));

        return documentRepository
            .findAllByStudentId(studentId).
            stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
        
    }

}
