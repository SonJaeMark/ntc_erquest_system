package com.github.sonjaemark.ntc_erquest_system.dto;

import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;

public record PaymentRequestDTO(
    PaymentMethod paymentMethod,
    Long documentRequestId,
    String referenceNumber
) {
    
}
