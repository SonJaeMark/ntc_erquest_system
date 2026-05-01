package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sonjaemark.ntc_erquest_system.model.RequestLogs;

public interface RequestLogsRepository extends JpaRepository<RequestLogs, Long>{

    List<RequestLogs> findAllByDocumentRequestId(Long id);

}
