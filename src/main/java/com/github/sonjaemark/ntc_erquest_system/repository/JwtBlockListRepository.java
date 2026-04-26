package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sonjaemark.ntc_erquest_system.model.JwtBlockList;

@Repository
public interface JwtBlockListRepository extends JpaRepository<JwtBlockList, Long>{

    Optional<JwtBlockList> findByAccessToken(String accessToken);

}