package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
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
        this.paymentMapper = paymentMapper;
    }

    @Override 
    public PaymentResponseDTO pay() {
        isAuthorized(List.of(UserRole.STUDENT));
        return save(paymentMapper.mapToPayment(getPaymentRequestDTO()));
    }

    @Override
    public PaymentResponseDTO validatePayment(Long paymentId) {
        isAuthorized(List.of(UserRole.REGISTRAR));
        return save(paymentRepository.findById(paymentId).orElseThrow(), p -> p.setValidated(true));
    }

    private PaymentResponseDTO save(Payment payment) {
        return mapToResponse(paymentRepository.save(payment));
    }

    private PaymentResponseDTO save(Payment payment, Consumer<Payment> mutator) {
        mutator.accept(payment);
        return mapToResponse(paymentRepository.save(payment));
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {
        return paymentMapper.mapToPaymentResponseDTO(payment);
    }

    public List<PaymentResponseDTO> listPendingPayments() {
    isAuthorized(List.of(UserRole.REGISTRAR));
    return paymentRepository.findByValidatedIsNullOrValidatedIsFalse()
        .stream()
        .map(paymentMapper::mapToPaymentResponseDTO)
        .toList();
    }
}