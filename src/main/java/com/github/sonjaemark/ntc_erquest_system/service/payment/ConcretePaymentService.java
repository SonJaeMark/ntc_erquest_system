package com.github.sonjaemark.ntc_erquest_system.service.payment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConcretePaymentService extends AbstractPaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public ConcretePaymentService(AuthService authService, PaymentRepository paymentRepository, PaymentMapper paymentMapper){
        super(authService);
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override 
    public PaymentResponseDTO pay() {
        isAuthorized(List.of(UserRole.STUDENT));

        Payment payment = paymentMapper.mapToPayment(getPaymentRequestDTO());

        return paymentMapper.mapToPaymentResponseDTO(paymentRepository.save(payment));
    }
}