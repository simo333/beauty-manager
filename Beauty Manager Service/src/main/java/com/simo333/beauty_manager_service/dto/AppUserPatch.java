package com.simo333.beauty_manager_service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AppUserPatch {
    @Size(min = 8, max = 120)
    private String password;
    @Pattern(regexp = "^\\+\\d{11}$")
    private String phoneNumber;
}
