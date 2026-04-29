package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_requests_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Purpose purpose; // fixed purpose also can be added in the future

    private String additionalDetails; // to be filled by student

    private String remarks; // remarks of Registrar

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document; 

    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserModel student;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserModel registrar;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.requestedAt = now;
        this.updatedAt = now;
        this.status = RequestStatus.PENDING;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}