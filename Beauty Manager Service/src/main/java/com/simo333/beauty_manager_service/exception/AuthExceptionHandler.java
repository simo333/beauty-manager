package com.simo333.beauty_manager_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleTokenRefreshException(RefreshTokenException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = {InternalAuthenticationServiceException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAuthExceptions(WebRequest request) {
        String message = "Bad credentials.";
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAccessDeniedException(WebRequest request) {
        String message = "Access denied.";
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                message,
                request.getDescription(false));
    }
}
