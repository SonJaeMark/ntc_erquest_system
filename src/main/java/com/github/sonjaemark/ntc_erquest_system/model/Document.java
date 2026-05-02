package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private static final double DEFAULT_AMOUNT = 120.00;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private UserModel student;

    private String documentContent;

    @PrePersist
    void onCreate() {
        this.uploadedAt = LocalDateTime.now();

        if (this.amount <= 0) {
            this.amount = DEFAULT_AMOUNT;
        }
    }
}