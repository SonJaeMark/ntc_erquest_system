package com.github.sonjaemark.ntc_erquest_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sonjaemark.ntc_erquest_system.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
