package com.github.sonjaemark.ntc_erquest_system.service.requestLogs;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.RequestLogs;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.RequestLogsRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConcreteRequestLogsService extends AbstractRequestLogsService implements IRequestLogsQueryService{

    private final RequestLogsRepository requestLogsRepository;

    protected ConcreteRequestLogsService(AuthService authService, RequestLogsRepository requestLogsRepository) {
        super(authService);
        this.requestLogsRepository = requestLogsRepository;
    }

    @Override
    public RequestLogsResponseDTO logAction(DocumentRequest documentRequest, String remarks) {
        isAuthorized(List.of(UserRole.REGISTRAR, UserRole.STUDENT));
        RequestLogs requestLogs = mapToRequestLogs(documentRequest, remarks);    
        return mapToRequestLogsResponseDTO(requestLogsRepository.save(requestLogs));
    }

    @Override
    public List<RequestLogsResponseDTO> getRequestLogsByDocumentRequestId(Long id) {
        isAuthorized(List.of(UserRole.REGISTRAR, UserRole.STUDENT));
        return requestLogsRepository
            .findAllByDocumentRequestId(id)
            .stream()
            .map(this::mapToRequestLogsResponseDTO)
            .toList();
    }

}
