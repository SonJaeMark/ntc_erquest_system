package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {
    List<DocumentRequest> findByStudentId(Long studentId);
    List<DocumentRequest> findByStatus(RequestStatus status);
    List<DocumentRequest> findByStatusIn(List<RequestStatus> statuses);
    List<DocumentRequest> findByRegistrarId(Long registrarId);

    List<DocumentRequest> findByDocumentTypeAndStudentIdAndStatus(
        DocumentType documentType, 
        Long studentId, 
        RequestStatus status
    );

    boolean existsByStudentIdAndStatusIn(Long studentId, List<RequestStatus> statuses);
}
   
