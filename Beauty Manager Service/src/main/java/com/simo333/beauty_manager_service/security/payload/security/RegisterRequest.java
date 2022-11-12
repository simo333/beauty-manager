package com.simo333.beauty_manager_service.security.payload.security;

import com.simo333.beauty_manager_service.security.payload.client.ClientRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegisterRequest extends ClientRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 120)
    private String password;
}
