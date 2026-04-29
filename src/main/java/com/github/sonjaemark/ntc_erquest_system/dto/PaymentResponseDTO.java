package com.github.sonjaemark.ntc_erquest_system.dto;

import java.time.LocalDateTime;

public record PaymentResponseDTO(
    Long paymentId,
    Boolean isPaid,
    Double amount,
    LocalDateTime paidAt,
    Long documentRequestId,
    Boolean validated,
    String referenceNumber
) {

}
