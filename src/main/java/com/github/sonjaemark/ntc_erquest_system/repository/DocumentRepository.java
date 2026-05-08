package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sonjaemark.ntc_erquest_system.model.Document;

import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{
    public List<Document> findAllByStudentId(Long id);
    public Optional<Document> findByStudentIdAndDocumentType(Long studentId, DocumentType documentType);
}
