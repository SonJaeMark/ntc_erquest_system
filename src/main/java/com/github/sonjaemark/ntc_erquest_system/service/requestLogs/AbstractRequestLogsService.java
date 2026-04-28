package com.github.sonjaemark.ntc_erquest_system.service.requestLogs;

import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.RequestLogs;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractRequestLogsService extends AuthLevel {

    private RequestLogsRequestDTO requestLogsRequestDTO;

    protected AbstractRequestLogsService(AuthService authService) {
        super(authService);
    }

    public RequestLogs mapToRequestLogs(RequestLogsRequestDTO requestLogsRequestDTO) {
        if (requestLogsRequestDTO == null) {
            return null;
        }

        return RequestLogs.builder()
                .documentRequest(DocumentRequest.builder()
                    .id(requestLogsRequestDTO.documentRequestId())
                    .build())
                .requestStatus(requestLogsRequestDTO.requestStatus())
                .remarks(requestLogsRequestDTO.remarks())
                // dateAction is usually handled by @PrePersist or manual setting in service
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
   
    
    public abstract RequestLogsResponseDTO logAction();

}
