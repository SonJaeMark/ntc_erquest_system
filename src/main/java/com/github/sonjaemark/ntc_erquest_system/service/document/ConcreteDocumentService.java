package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
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
    public DocumentResponseDTO save() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<DocumentResponseDTO> getStudentsDocuments() {
        Long studentId = isAuthorized(List.of(UserRole.STUDENT));

        return documentRepository
            .findById(studentId).
            stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
        
    }

}
