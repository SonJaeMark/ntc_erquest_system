package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;

public record PaymentRequestDTO(
    double amount,
    PaymentMethod paymentMethod,
    Long documentRequestId
) {
    
}
