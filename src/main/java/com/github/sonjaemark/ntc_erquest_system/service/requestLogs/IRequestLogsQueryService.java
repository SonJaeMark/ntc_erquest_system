package com.github.sonjaemark.ntc_erquest_system.service.requestLogs;

import java.util.List;

import com.github.sonjaemark.ntc_erquest_system.dto.RequestLogsResponseDTO;

public interface IRequestLogsQueryService {
    List<RequestLogsResponseDTO> getRequestLogsByDocumentRequestId(Long id);  
}
