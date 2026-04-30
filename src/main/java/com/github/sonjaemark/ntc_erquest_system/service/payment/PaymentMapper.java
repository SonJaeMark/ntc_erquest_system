package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.InvalidPaymentException;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;

@Component
public class PaymentMapper {
    
    private final DocumentRequestRepository documentRequestRepository;

    public PaymentMapper(DocumentRequestRepository documentRequestRepository){
        this.documentRequestRepository = documentRequestRepository;
    }

    public Payment mapToPayment(PaymentRequestDTO dto){
        String ref = dto.paymentMethod() == PaymentMethod.CASH 
            ? UUID.randomUUID().toString()
            : validateReference(dto.referenceNumber(), dto.paymentMethod());

        return Payment.builder()
            .amount(dto.amount())
            .validated(false)
            .paymentMethod(dto.paymentMethod())
            .referenceNumber(ref)
            .documentrequest(documentRequestRepository.findById(dto.documentRequestId()).orElseThrow())
            .build();
    }

    private String validateReference(String ref, PaymentMethod method) {
        if (ref == null || ref.isBlank() || !ref.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new InvalidPaymentException(method + " payment requires a valid reference number");
        }
        return ref;
    }

    public PaymentResponseDTO mapToPaymentResponseDTO(Payment payment){
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getPaidAt() != null,
            payment.getAmount(),
            payment.getPaidAt(),
            payment.getDocumentrequest().getId(),
            payment.getValidated(),
            payment.getReferenceNumber()
        );
    }
}
