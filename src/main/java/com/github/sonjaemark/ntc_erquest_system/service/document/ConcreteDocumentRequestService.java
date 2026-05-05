package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.DocumentRequestInvalidStatusException;
import com.github.sonjaemark.ntc_erquest_system.exception.IdNotFoundException;
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
            AbstractRequestLogsService requestLogsService
    ) {
        super(userModelRepository, documentRepository, authService, requestLogsService);
        this.documentRequestRepository = documentRequestRepository;
    }

    @Override
    public DocumentRequestResponseDTO submit(DocumentRequestRequestDTO documentRequestDTO) {
        System.out.println("DEBUG: Submit request received: " + documentRequestDTO);
        isAuthorized(List.of(UserRole.STUDENT));

        // For new submissions, ensure ID is null to prevent accidental updates
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setId(null);

        boolean hasActiveRequest = documentRequestRepository.existsByStudentIdAndStatusIn(
            documentRequest.getStudent().getId(),
            List.of(
                RequestStatus.PENDING,
                RequestStatus.PAID,
                RequestStatus.VALIDATED,
                RequestStatus.PROCESSING,
                RequestStatus.READY_FOR_RELEASE
            )
        );

        if (hasActiveRequest) {
            throw new DocumentRequestAlreadyExistException(
                "You already have an active request. Please wait until your current document is claimed or released before submitting another request."
            );
        }

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Student submitted a document request for " + savedDocumentRequest.getDocumentType(), savedDocumentRequest.getStatus());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO process(DocumentRequestRequestDTO documentRequestDTO) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        if (documentRequestDTO.id() == null) {
            throw new IdNotFoundException("Document Request ID is required for processing");
        }

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Registrar updated the request status to " + savedDocumentRequest.getStatus(), savedDocumentRequest.getStatus());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO accept(DocumentRequestRequestDTO documentRequestDTO) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        if (documentRequestDTO.id() == null) {
            throw new IdNotFoundException("Document Request ID is required for acceptance");
        }

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setStatus(RequestStatus.PROCESSING);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Registrar accepted the request", savedDocumentRequest.getStatus());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    public List<DocumentRequestResponseDTO> getAllByStudentId(Long studentId) {
        isAuthorized(List.of(UserRole.STUDENT));

        return documentRequestRepository.findByStudentId(studentId)
            .stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }

    public List<DocumentRequestResponseDTO> getAllUnacceptedRequest() {
        isAuthorized(List.of(UserRole.REGISTRAR));

        return documentRequestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.PAID))
            .stream()
            .filter(request -> request.getRegistrar() == null)
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }

    public List<DocumentRequestResponseDTO> getAllByRegistrarId(Long registrarId) {
        

        return documentRequestRepository.findByRegistrarId(registrarId)
            .stream()
            .filter(request -> 
                request.getStatus().equals(RequestStatus.PAID) ||
                request.getStatus().equals(RequestStatus.VALIDATED) ||
                request.getStatus().equals(RequestStatus.PROCESSING) || 
                request.getStatus().equals(RequestStatus.READY_FOR_RELEASE) || 
                request.getStatus().equals(RequestStatus.RELEASED) ||
                request.getStatus().equals(RequestStatus.REJECTED)
            )
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }

    @Override
    public List<DocumentRequestResponseDTO> getAllDocumentRequestByStudentId() {
        Long id = isAuthorized(List.of(UserRole.STUDENT));
        return getAllByStudentId(id);
    }

    @Override
    public List<DocumentRequestResponseDTO> getAllAceptedRequestByRegistrarId() {
        Long id = isAuthorized(List.of(UserRole.REGISTRAR));
        return getAllByRegistrarId(id);
    }

    @Override
    public DocumentRequestResponseDTO cancelRequest(Long documentRequestId){
        isAuthorized(List.of(UserRole.STUDENT));

        DocumentRequest documentRequest = documentRequestRepository
            .findById(documentRequestId)
            .orElseThrow(() -> new DocumentRequestInvalidStatusException("Document request not found"));

        if (!documentRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new DocumentRequestInvalidStatusException("Only pending requests can be cancelled");
        }
        documentRequest.setStatus(RequestStatus.CANCELLED);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Student cancelled the request", savedDocumentRequest.getStatus());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }
}
