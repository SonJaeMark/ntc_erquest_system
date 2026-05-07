package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.InvalidPaymentException;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;

@Component
public class PaymentMapper {
    
    private final DocumentRequestRepository documentRequestRepository;

    public PaymentMapper(DocumentRequestRepository documentRequestRepository) {
        this.documentRequestRepository = documentRequestRepository;
    }

    public Payment mapToPayment(PaymentRequestDTO dto) {
        DocumentRequest documentRequest = documentRequestRepository
                .findById(dto.documentRequestId())
                .orElseThrow(() -> new InvalidPaymentException("Document request not found"));

        return Payment.builder()
                .validated(false)
                .paymentMethod(dto.paymentMethod())
                .referenceNumber(getReferenceNumber(dto))
                .documentrequest(documentRequest)
                .build();
    }

    private String getReferenceNumber(PaymentRequestDTO dto) {
        if (dto.paymentMethod() == PaymentMethod.CASH) {
            return "CASH-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();
        }

        return validateReference(dto.referenceNumber(), dto.paymentMethod());
    }

    private String validateReference(String referenceNumber, PaymentMethod method) {
        if (referenceNumber == null || referenceNumber.isBlank()) {
            throw new InvalidPaymentException(method + " payment requires a reference number");
        }

        if (!referenceNumber.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new InvalidPaymentException("Reference number can only contain letters, numbers, and dash");
        }

        return referenceNumber.trim().toUpperCase();
    }

    public PaymentResponseDTO mapToPaymentResponseDTO(Payment payment) {
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