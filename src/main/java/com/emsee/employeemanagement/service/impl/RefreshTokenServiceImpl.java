package com.emsee.employeemanagement.service.impl;

import com.emsee.employeemanagement.entity.RefreshToken;
import com.emsee.employeemanagement.entity.User;
import com.emsee.employeemanagement.repository.RefreshTokenRepository;
import com.emsee.employeemanagement.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    //7 Days, 24 hours, 60 minutes, 60 seconds, 1000 Milliseconds -> 604800000 milliseconds which is 7 days
    private final long refreshTokenDurationMs = 7 * 24 * 60 * 60 * 1000L;


    @Override
    public RefreshToken createRefreshToken(User user) {
        // Remove existing refresh token if present
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        // check if token expired
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired. Please login again.");
        }
        return token;
    }


    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(refreshToken);
    }
}