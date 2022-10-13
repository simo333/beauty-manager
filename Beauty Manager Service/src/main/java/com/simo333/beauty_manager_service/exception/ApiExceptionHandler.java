package com.simo333.beauty_manager_service.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleTokenRefreshException(RefreshTokenException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = {InternalAuthenticationServiceException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthExceptions(WebRequest request) {
        String message = "Bad credentials.";
        return new ExceptionResponse(
                HttpStatus.UNAUTHORIZED,
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

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageNotReadableException(WebRequest request) {
        String message = "Request body is missing or some of inputs are incorrect.";
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorsMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorsMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleSQLIntegrityConstraintViolationException(WebRequest request) {
        String message = "Cannot delete or update a parent row: a foreign key constraint fails.";
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String message = String.format("'%s' request method not supported.", ex.getMethod());
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpRequestMethodNotSupportedException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = RequestRejectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleRequestRejectedException(RequestRejectedException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = FreeBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleFreeBusyException(FreeBusyException ex, WebRequest request) {
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }


}
