package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.InvalidPaymentException;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import com.github.sonjaemark.ntc_erquest_system.service.requestLogs.AbstractRequestLogsService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConcretePaymentService extends AbstractPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final DocumentRequestRepository documentRequestRepository;

    public ConcretePaymentService(
            AuthService authService,
            AbstractRequestLogsService requestLogsService,
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            DocumentRequestRepository documentRequestRepository
    ) {
        super(authService, requestLogsService);
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.documentRequestRepository = documentRequestRepository;
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
        payment.getDocumentrequest().setStatus(RequestStatus.PAID);
        logAction(payment.getDocumentrequest(), "Student paid", RequestStatus.PAID);
        return save(payment);
    }

    @Override
    public PaymentResponseDTO validatePayment(Long paymentId) {
        isAuthorized(List.of(UserRole.REGISTRAR));
        Payment payment = findPayment(paymentId);
        payment.getDocumentrequest().setStatus(RequestStatus.VALIDATED);
        logAction(payment.getDocumentrequest(), "Registrar validated the payment", RequestStatus.VALIDATED);
        
        return save(
                payment,
                paid -> paid.setValidated(true)
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
    public  PaymentResponseDTO checkPayment(Long documentRequestId) {
        return mapToResponse(paymentRepository.findPaymentByDocumentrequestId(documentRequestId).orElseThrow(() -> new InvalidPaymentException("Payment not found")));
    }

    @Override
    public PaymentResponseDTO confirmPayment(Long documentRequestId) {
        isAuthorized(List.of(UserRole.REGISTRAR));

        documentRequestRepository.findById(documentRequestId)
                .orElseThrow(() -> new InvalidPaymentException("Document request not found"));
        
        Payment payment = paymentRepository.findPaymentByDocumentrequestId(documentRequestId)
                .orElseThrow(() -> new InvalidPaymentException("Payment not found"));
        
        payment.getDocumentrequest().setStatus(RequestStatus.VALIDATED);
        logAction(payment.getDocumentrequest(), "Registrar confirmed the payment", RequestStatus.VALIDATED);
        save(
                payment,
                paid -> paid.setValidated(true)
        );

        payment.getDocumentrequest().setStatus(RequestStatus.READY_FOR_RELEASE);
        logAction(payment.getDocumentrequest(), "Document request is ready for release", RequestStatus.READY_FOR_RELEASE);
        return save(payment);
    }
}
