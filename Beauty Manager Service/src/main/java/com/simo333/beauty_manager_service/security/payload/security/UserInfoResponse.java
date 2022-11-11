package com.simo333.beauty_manager_service.security.payload.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private String email;
    private Set<String> roles;
}

