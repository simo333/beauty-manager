package com.simo333.beauty_manager_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleTokenRefreshException(RefreshTokenException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
    }
}
