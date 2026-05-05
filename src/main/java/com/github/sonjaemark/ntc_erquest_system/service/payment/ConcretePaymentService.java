package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;
import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.InvalidPaymentException;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConcretePaymentService extends AbstractPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public ConcretePaymentService(
            AuthService authService,
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper
    ) {
        super(authService);
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override 
    public PaymentResponseDTO pay() {
        isAuthorized(List.of(UserRole.STUDENT));

        PaymentRequestDTO paymentRequestDTO = getPaymentRequestDTO();

        if (paymentRepository.existsByDocumentrequestId(paymentRequestDTO.documentRequestId())) {
            throw new InvalidPaymentException("This document request already has a payment");
        }

        Payment payment = paymentMapper.mapToPayment(paymentRequestDTO);

        payment.setAmount(
                payment.getDocumentrequest()
                        .getDocument()
                        .getAmount()
        );

        if (paymentRepository.existsByReferenceNumber(payment.getReferenceNumber())) {
            throw new InvalidPaymentException("Reference number already exists");
        }

        return save(payment);
    }

    @Override
    public PaymentResponseDTO validatePayment(Long paymentId) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        return save(
                findPayment(paymentId),
                payment -> payment.setValidated(true)
        );
    }

    public List<PaymentResponseDTO> listPendingPayments() {
        isAuthorized(List.of(UserRole.REGISTRAR));

        return paymentRepository.findByValidatedIsNullOrValidatedIsFalse()
                .stream()
                .map(paymentMapper::mapToPaymentResponseDTO)
                .toList();
    }

    private Payment findPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new InvalidPaymentException("Payment not found"));
    }

    private PaymentResponseDTO save(Payment payment) {
        return mapToResponse(paymentRepository.save(payment));
    }

    private PaymentResponseDTO save(Payment payment, Consumer<Payment> mutator) {
        mutator.accept(payment);
        return save(payment);
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {
        return paymentMapper.mapToPaymentResponseDTO(payment);
    }

    @Override
    public  PaymentResponseDTO checkPayment(Long paymentId) {
        return mapToResponse(paymentRepository.findPaymentByDocumentrequestId(paymentId).orElseThrow(() -> new InvalidPaymentException("Payment not found")));
    }
}