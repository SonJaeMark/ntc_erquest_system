package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.DocumentRequestAlreadyExistException;
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
    public DocumentResponseDTO submit() {
    isAuthorized(List.of(UserRole.STUDENT));

    DocumentRequestDTO documentRequestDTO = getDocumentRequestDTO();

    Long studentId = documentRequestDTO.studentId();

    boolean hasActiveRequest = documentRequestRepository.existsByStudentIdAndStatusIn(
        studentId,
        List.of(
            RequestStatus.PENDING,
            RequestStatus.PROCESSING,
            RequestStatus.READY_FOR_RELEASE
        )
    );

    if (hasActiveRequest) {
        throw new DocumentRequestAlreadyExistException(
            "You already have an active request. Please wait until your current document is claimed or released before submitting another request."
        );
    }

    logAction(RequestStatus.PENDING);

    DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

    return mapToDocumentResponseDTO(documentRequestRepository.save(documentRequest));
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
