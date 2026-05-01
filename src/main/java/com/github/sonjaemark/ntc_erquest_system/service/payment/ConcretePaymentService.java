package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.exception.IdNotFoundException;
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

    public ConcretePaymentService(AuthService authService, PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        super(authService);
        this.paymentRepository = paymentRepository;
        this.paymentMapper     = paymentMapper;
    }

    @Override
    public PaymentResponseDTO pay() {
        isAuthorized(List.of(UserRole.STUDENT));
        return paymentMapper.mapToPaymentResponseDTO(
            paymentRepository.save(paymentMapper.mapToPayment(getPaymentRequestDTO()))
        );
    }

    @Override
    public PaymentResponseDTO validatePayment(Long paymentId) {
        isAuthorized(List.of(UserRole.REGISTRAR));
        Payment payment = findById(paymentId);
        payment.setValidated(true);
        return paymentMapper.mapToPaymentResponseDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {
        isAuthorized(List.of(UserRole.REGISTRAR, UserRole.STUDENT));
        return paymentMapper.mapToPaymentResponseDTO(findById(paymentId));
    }

    @Override
    public List<PaymentResponseDTO> listPendingPayments() {
        isAuthorized(List.of(UserRole.REGISTRAR));
        return paymentRepository.findByValidatedIsNullOrValidatedIsFalse()
            .stream()
            .map(paymentMapper::mapToPaymentResponseDTO)
            .toList();
    }

    private Payment findById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Payment not found: " + id));
    }
}
