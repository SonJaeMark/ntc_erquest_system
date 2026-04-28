package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    private LocalDateTime paid_at;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @PrePersist
    void onCreate() {
        this.paid_at = LocalDateTime.now();
    }
}