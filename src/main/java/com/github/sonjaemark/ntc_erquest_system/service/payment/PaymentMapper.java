package com.github.sonjaemark.ntc_erquest_system.service.payment;

import org.springframework.stereotype.Component;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;

@Component
public class PaymentMapper {
    
    private final DocumentRequestRepository documentRequestRepository;

    public PaymentMapper(DocumentRequestRepository documentRequestRepository){
        this.documentRequestRepository = documentRequestRepository;
    }
    public Payment mapToPayment(PaymentRequestDTO paymentRequestDTO){
        return Payment.builder()
        .amount(paymentRequestDTO.amount())
        .paymentMethod(paymentRequestDTO.paymentMethod())
        .documentrequest(documentRequestRepository.findById(paymentRequestDTO.documentRequestId()).orElseThrow())
        .build();
    }

    public PaymentResponseDTO mapToPaymentResponseDTO(Payment payment){
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getPaidAt() != null? true : false,
            payment.getAmount(),
            payment.getPaidAt(),
            payment.getDocumentrequest().getId()
        );
        
    }
}
