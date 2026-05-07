package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="request_logs_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_request_id")
    private DocumentRequest documentRequest;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private LocalDateTime dateAction;

    private String remarks;

    @PrePersist
    public void onCreate(){
        this.dateAction = LocalDateTime.now();
    }
}
