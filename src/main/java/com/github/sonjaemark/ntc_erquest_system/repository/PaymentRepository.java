package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sonjaemark.ntc_erquest_system.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByValidatedIsNullOrValidatedIsFalse();

    boolean existsByDocumentrequestId(Long documentRequestId);

    boolean existsByReferenceNumber(String referenceNumber);

    Optional<Payment> findPaymentByDocumentrequestId(Long documentRequestId);
}
