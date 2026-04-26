package com.github.sonjaemark.ntc_erquest_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {
}