package com.simo333.beauty_manager_service.service.impl;

import com.simo333.beauty_manager_service.exception.RefreshTokenException;
import com.simo333.beauty_manager_service.model.RefreshToken;
import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.repository.RefreshTokenRepository;
import com.simo333.beauty_manager_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.security.refresh-token.expirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> {
            log.error("Refresh token not found: {}", token);
            throw new RefreshTokenException(token, "Refresh token not found in database.");
        });
        log.info("Refresh token has been found.");
        return refreshToken;
    }

    @Override
    public RefreshToken create(String userEmail) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(principal);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        log.info("New refresh token created for user '{}'", userEmail);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            log.error("Refresh token has expired and has been deleted from database. For refresh token: {}", token);
            return false;
        }
        log.info("Token is up-to-date. For token: {}", token);
        return true;
    }

    @Override
    public void deleteByUser(User user) {
        log.info("Deleting refresh token for user with id '{}'", user.getId());
        refreshTokenRepository.deleteByUser(user);
    }
}
