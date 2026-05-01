package com.github.sonjaemark.ntc_erquest_system.service.token;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.exception.IdNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.exception.RefreshTokenExpiredException;
import com.github.sonjaemark.ntc_erquest_system.model.RefreshToken;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.repository.RefreshTokenRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository repository;

    @Autowired
    private UserModelRepository userRepository;

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("User not found"));

        // Delete existing refresh tokens for the user to avoid accumulation
        repository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return repository.save(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            repository.delete(token);
            throw new RefreshTokenExpiredException("Refresh token expired. Please log in again.");
        }

        return token;
    }
}
