package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;

import org.jspecify.annotations.Nullable;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AbstractPaymentService extends AuthLevel {

    private PaymentRequestDTO paymentRequestDTO; 

    protected AbstractPaymentService(AuthService authService){
        super(authService);
    }

    public abstract PaymentResponseDTO pay();
    
    public abstract PaymentResponseDTO validatePayment(Long paymentId);

    
    public abstract List<PaymentResponseDTO> listPendingPayments();

    public abstract PaymentResponseDTO checkPayment(Long paymentId);
    
}
