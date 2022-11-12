package com.simo333.beauty_manager_service.security.payload.client;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ClientRequest {
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
