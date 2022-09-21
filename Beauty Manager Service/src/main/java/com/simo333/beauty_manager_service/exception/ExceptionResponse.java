package com.simo333.beauty_manager_service.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
public class ExceptionResponse {
    private final HttpStatus httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]", timezone = "UTC+2")
    private final Instant timeStamp = Instant.now();
    private final String message;
    private final String description;

    public ExceptionResponse(HttpStatus httpStatus, String message, String description) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
