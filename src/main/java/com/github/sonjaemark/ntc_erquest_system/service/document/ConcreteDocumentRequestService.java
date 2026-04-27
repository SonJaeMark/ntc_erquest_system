package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;



@Service
@Transactional
public class ConcreteDocumentRequestService extends AbstractDocumentRequestService {

    private final DocumentRequestRepository documentRequestRepository;

    public ConcreteDocumentRequestService(
            DocumentRequestRepository documentRequestRepository, 
            UserModelRepository userModelRepository,
            DocumentRepository documentRepository) {
        super(userModelRepository, documentRepository); // Pass to Abstract class
        this.documentRequestRepository = documentRequestRepository;
    }

    @Override
    public DocumentResponseDTO submit() {
        isAuthorized(List.of(UserRole.STUDENT));
        logAction(RequestStatus.PENDING);

        // Validation of request

        DocumentRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    @Override
    public DocumentResponseDTO process() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        

        DocumentRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        logAction(documentRequest.getStatus());

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    @Override
    public DocumentResponseDTO accept() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        logAction(RequestStatus.PROCESSING);

        DocumentRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    public List<DocumentResponseDTO> getAllByStudentId(Long studentId) {
        isAuthorized(List.of(UserRole.STUDENT));

        return documentRequestRepository.findByStudentId(studentId) // Get the list of entities
            .stream()                                               // Open a stream
            .map(this::mapToDocumentResponseDTO)                    // Convert each entity to DTO
            .toList();                                              // Collect back into a List
    }

    public List<DocumentResponseDTO> getAllUnacceptedRequest() {
        isAuthorized(List.of(UserRole.REGISTRAR)); 

        return documentRequestRepository.findByStatus(RequestStatus.PENDING)    // Get all Request with PENDING status
            .stream()                                                           // Open a stream
            .map(this::mapToDocumentResponseDTO)                                // Convert each entity to DTO
            .toList();                                                          // Collect back into a List
    }

    public List<DocumentResponseDTO> getAllByRegistrarId(Long registrarId) {
        isAuthorized(List.of(UserRole.REGISTRAR)); 

        return documentRequestRepository.findByRegistrarId(registrarId)
            .stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }


}
