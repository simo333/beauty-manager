package com.simo333.beauty_manager_service.service;

import com.simo333.beauty_manager_service.model.User;
import com.simo333.beauty_manager_service.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken create(String userEmail);

    RefreshToken findByToken(String token);

    boolean verifyExpiration(RefreshToken token);

    void deleteByUser(User user);
}
