package com.github.sonjaemark.ntc_erquest_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.dto.PaymentRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.PaymentResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.service.payment.AbstractPaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final AbstractPaymentService paymentService;

    public PaymentController(AbstractPaymentService paymentService){
        this.paymentService = paymentService;
    }
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> pay(@RequestBody PaymentRequestDTO paymentRequestDTO){
        paymentService.setPaymentRequestDTO(paymentRequestDTO);
        return ResponseEntity.ok(paymentService.pay());
    }
}
