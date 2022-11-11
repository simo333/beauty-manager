package com.simo333.beauty_manager_service.security.payload.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 120)
    private String password;
    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 150)
    private String lastName;
    @NotNull
    @Pattern(regexp = "^\\+\\d{11}$")
    private String phoneNumber;
}
