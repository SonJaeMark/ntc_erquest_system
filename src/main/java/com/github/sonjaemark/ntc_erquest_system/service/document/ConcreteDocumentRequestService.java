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
public class ConcreteDocumentRequestService extends AbstractDocumentRequestService {

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
    public DocumentRequestResponseDTO submit() {
        isAuthorized(List.of(UserRole.STUDENT));

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();

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

        logAction();

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO process() {
        isAuthorized(List.of(UserRole.REGISTRAR));

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction();

        return mapToDocumentResponseDTO(savedDocumentRequest);
    }

    @Override
    public DocumentRequestResponseDTO accept() {
        isAuthorized(List.of(UserRole.REGISTRAR));

        DocumentRequestRequestDTO documentRequestDTO = getDocumentRequestDTO();
        DocumentRequest documentRequest = mapToDocumentRequest(documentRequestDTO);
        documentRequest.setStatus(RequestStatus.PROCESSING);

        DocumentRequest savedDocumentRequest = documentRequestRepository.save(documentRequest);

        logAction();

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
}