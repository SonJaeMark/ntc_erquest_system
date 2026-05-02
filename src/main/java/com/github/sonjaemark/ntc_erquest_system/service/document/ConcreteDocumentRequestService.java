package com.github.sonjaemark.ntc_erquest_system.service.document;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.DocumentRequestResponseDTO;
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

        boolean hasActiveRequest = documentRequestRepository.existsByStudentIdAndStatusIn(
            documentRequestDTO.studentId(),
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

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Student submitted a document request for " + savedDocumentRequest.getDocumentType());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO process(DocumentRequestRequestDTO documentRequestDTO) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Registrar updated the request status to " + savedDocumentRequest.getStatus());

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO accept(DocumentRequestRequestDTO documentRequestDTO) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setStatus(RequestStatus.PROCESSING);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction(savedDocumentRequest, "Registrar accepted the request");

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

        return documentRequestRepository.findByStatus(RequestStatus.PENDING)
            .stream()
            .map(this::mapToDocumentResponseDTO)
            .toList();
    }

    public List<DocumentRequestResponseDTO> getAllByRegistrarId(Long registrarId) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        return documentRequestRepository.findByRegistrarId(registrarId)
            .stream()
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllAceptedRequestByRegistrarId'");
    }
}