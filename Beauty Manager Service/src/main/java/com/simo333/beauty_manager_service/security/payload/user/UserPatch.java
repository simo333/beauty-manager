package com.simo333.beauty_manager_service.security.payload.user;

import com.simo333.beauty_manager_service.model.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UserPatch {
    @Email
    private String email;
    @Size(min = 8, max = 120)
    private String password;
    private Client client;
}
