package com.simo333.beauty_manager_service.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
    private static final String TYPE = "Bearer";
    private String email;
    private Set<String> roles;
}
