package com.github.sonjaemark.ntc_erquest_system.service.requestLogs;

import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.RequestLogs;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractRequestLogsService extends AuthLevel {

    protected AbstractRequestLogsService(AuthService authService) {
        super(authService);
    }

    public RequestLogs mapToRequestLogs(DocumentRequest documentRequest, String remarks) {
        if (documentRequest == null) {
            return null;
        }

        return RequestLogs.builder()
                .documentRequest(documentRequest)
                .requestStatus(documentRequest.getStatus())
                .remarks(remarks)
                .build();
    }

    public RequestLogsResponseDTO mapToRequestLogsResponseDTO(RequestLogs requestLogs) {
        if (requestLogs == null) {
            return null;
        }

        return new RequestLogsResponseDTO(
                requestLogs.getId(),
                requestLogs.getRequestStatus(),
                requestLogs.getDateAction(),
                requestLogs.getRemarks()
        );
    }

    public abstract RequestLogsResponseDTO logAction(DocumentRequest documentRequest, String remarks);
}
