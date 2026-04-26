package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.sonjaemark.ntc_erquest_system.model.RefreshToken;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(UserModel user);
}