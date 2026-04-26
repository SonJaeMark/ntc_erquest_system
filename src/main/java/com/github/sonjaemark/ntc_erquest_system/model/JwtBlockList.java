package com.github.sonjaemark.ntc_erquest_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "jtw_block_list_table")
@AllArgsConstructor
@NoArgsConstructor
public class JwtBlockList {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accessToken;
    private LocalDateTime blockDateTime;
}

