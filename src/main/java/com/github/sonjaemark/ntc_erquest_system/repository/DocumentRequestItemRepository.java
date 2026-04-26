package com.github.sonjaemark.ntc_erquest_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequestItem;

public interface DocumentRequestItemRepository extends JpaRepository<DocumentRequestItem, Long> {
}