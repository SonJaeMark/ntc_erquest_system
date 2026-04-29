package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.DocumentNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.AbstractRequestLogsService;



@Service
@Transactional
public class ConcreteDocumentRequestService extends AbstractDocumentRequestService implements IDocumentRequestQueryService{

    private final DocumentRequestRepository documentRequestRepository;

    public ConcreteDocumentRequestService(
            DocumentRequestRepository documentRequestRepository, 
            UserModelRepository userModelRepository,
            DocumentRepository documentRepository,
            AuthService authService,
            AbstractRequestLogsService requestLogsService) {
        super(userModelRepository, documentRepository, authService, requestLogsService); // Pass to Abstract class
        this.documentRequestRepository = documentRequestRepository;
        
    }

    @Override
    public DocumentRequestResponseDTO submit() {
        Long id = isAuthorized(List.of(UserRole.STUDENT));
    
        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        if (!documentRepository.findAllByStudentId(id).stream().anyMatch(doc -> {
                    return doc.getDocumentType().equals(documentRequestDTO.documentType());
                })) {
            throw new DocumentNotFoundException("Cannot proccess document request, document not available");
        };

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setStudent(userModelRepository.findById(id).orElseThrow());

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);  // saving document request

        requestLogsService.setRequestLogsRequestDTO(
            new RequestLogsRequestDTO(savedDocumentRequest.getId(), savedDocumentRequest.getStatus(), savedDocumentRequest.getRemarks())
        );
        
        logAction();  // logging document request status

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO process() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);  // saving document request

        requestLogsService.setRequestLogsRequestDTO(
            new RequestLogsRequestDTO(savedDocumentRequest.getId(), savedDocumentRequest.getStatus(), savedDocumentRequest.getRemarks())
        );
        
        logAction();  // logging document request status

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO accept() {
        isAuthorized(List.of(UserRole.REGISTRAR));

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        documentRequest.setStatus(RequestStatus.PROCESSING);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);  // saving document request

        requestLogsService.setRequestLogsRequestDTO(
            new RequestLogsRequestDTO(savedDocumentRequest.getId(), RequestStatus.PROCESSING, savedDocumentRequest.getRemarks())
        );
        
        logAction();  // logging document request status

        return mapToDocumentResponseDTO(savedDocumentRequest);
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
