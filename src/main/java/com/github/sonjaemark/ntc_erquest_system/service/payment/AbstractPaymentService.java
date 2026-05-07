package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;


import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.AbstractRequestLogsService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractPaymentService extends AuthLevel {

    private PaymentRequestDTO paymentRequestDTO; 
    protected final AbstractRequestLogsService requestLogsService;
    
    protected AbstractPaymentService(AuthService authService, AbstractRequestLogsService requestLogsService){
        super(authService);
        this.requestLogsService = requestLogsService;
    }

    protected boolean logAction(DocumentRequest documentRequest, String remarks, RequestStatus statusBefore) {  
        requestLogsService.logAction(documentRequest, remarks, statusBefore);
        return true;
    }

    public abstract PaymentResponseDTO pay();
    
    public abstract PaymentResponseDTO validatePayment(Long paymentId);

    
    public abstract List<PaymentResponseDTO> listPendingPayments();

    public abstract PaymentResponseDTO checkPayment(Long paymentId);

    public abstract PaymentResponseDTO confirmPayment(Long documentRequestId);
    
}
