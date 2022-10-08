package com.simo333.beauty_manager_service.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ExceptionResponse {
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
    private final String message;
    private final String description;

    public ExceptionResponse(HttpStatus httpStatus, String message, String description) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
