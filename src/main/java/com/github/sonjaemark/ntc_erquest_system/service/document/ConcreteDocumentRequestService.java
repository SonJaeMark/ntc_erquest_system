package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.DocumentNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;



@Service
@Transactional
public class ConcreteDocumentRequestService extends AbstractDocumentRequestService implements IDocumentRequestQueryService{

    private final DocumentRequestRepository documentRequestRepository;

    public ConcreteDocumentRequestService(
            DocumentRequestRepository documentRequestRepository, 
            UserModelRepository userModelRepository,
            DocumentRepository documentRepository,
            AuthService authService) {
        super(userModelRepository, documentRepository, authService); // Pass to Abstract class
        this.documentRequestRepository = documentRequestRepository;
    }

    @Override
    public DocumentRequestResponseDTO submit() {
        Long id = isAuthorized(List.of(UserRole.STUDENT));
        logAction(RequestStatus.PENDING);

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        System.out.println("########################## STUDENT ID"+id+" ##########################");
        if (!documentRepository.findAllByStudentId(id).stream().anyMatch(doc -> {
                    return doc.getDocumentType().equals(documentRequestDTO.documentType());
                })) {
            throw new DocumentNotFoundException("Cannot proccess document request, document not available");
        };
        

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setStudent(userModelRepository.findById(id).orElseThrow());

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    @Override
    public DocumentRequestResponseDTO process() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        logAction(documentRequest.getStatus());

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    @Override
    public DocumentRequestResponseDTO accept() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        logAction(RequestStatus.PROCESSING);

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
    }

    public List<DocumentRequestResponseDTO> getAllDocumentRequestByStudentId() {
        Long studentId = isAuthorized(List.of(UserRole.STUDENT));

        return documentRequestRepository.findByStudentId(studentId) // Get the list of entities
            .stream()                                               // Open a stream
            .map(this::mapToDocumentResponseDTO)                    // Convert each entity to DTO
            .toList();                                              // Collect back into a List
    }

    public List<DocumentRequestResponseDTO> getAllUnacceptedRequest() {
        isAuthorized(List.of(UserRole.REGISTRAR)); 

        return documentRequestRepository.findByStatus(RequestStatus.PENDING)    // Get all Request with PENDING status
            .stream()                                                           // Open a stream
            .map(this::mapToDocumentResponseDTO)                                // Convert each entity to DTO
            .toList();                                                          // Collect back into a List
    }

    public List<DocumentRequestResponseDTO> getAllAceptedRequestByRegistrarId() {
        Long registrarId = isAuthorized(List.of(UserRole.REGISTRAR)); 

        return documentRequestRepository.findByRegistrarId(registrarId)
            .stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }

}
