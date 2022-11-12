package com.simo333.beauty_manager_service.security.payload.appointment;

import com.simo333.beauty_manager_service.model.Client;
import com.simo333.beauty_manager_service.model.Treatment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Getter
@Setter
public class AppointmentRequest {
    @NotNull
    @Valid
    private Treatment treatment;
    @NotNull
    @Valid
    private Client client;
    @NotNull
    @Future
    private ZonedDateTime dateTime;
}
