package com.github.sonjaemark.ntc_erquest_system.service.payment;

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
    
}
