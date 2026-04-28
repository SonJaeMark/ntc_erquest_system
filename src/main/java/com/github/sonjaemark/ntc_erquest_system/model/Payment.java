package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
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
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentRequest_id", nullable = false)
    private DocumentRequest documentrequest;
     
    @PrePersist
    void onCreate() {
        this.paidAt = LocalDateTime.now();
    }
}