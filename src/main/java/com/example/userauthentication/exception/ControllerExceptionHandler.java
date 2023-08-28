package com.example.userauthentication.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ResponseError> handleCredentialsException(AuthenticationException ex, HttpServletRequest request) {
        log.info("Known error {}: ", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "username or password is invalid",
                request,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({AccountStatusException.class})
    public ResponseEntity<ResponseError> handleAccountStatusException(AccountStatusException ex, HttpServletRequest request) {
        log.info("Known error {}: ", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "user account is disabled",
                request,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ResponseError> handleInvalidBearerTokenException(InvalidBearerTokenException ex, HttpServletRequest request) {
        log.info("Known error {}: ", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "invalid access token",
                request,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.info("Known error {}: ", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "access denied",
                request,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.info("Known error {}: ", ex.getClass().getSimpleName(), ex);

        return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
        log.info("Unknown error {}: ", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "Internal Server Error",
                request,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ResponseError> buildResponseEntity(String message, HttpServletRequest request, HttpStatus status) {
        String path = request.getRequestURI();
        var errorDetails = buildErrorResponse(status.value(), path, message);
        return new ResponseEntity<>(errorDetails, status);
    }

    private ResponseError buildErrorResponse(int statusCode, String path, String msg) {
        return ResponseError.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .message(msg)
                .path(path)
                .build();
    }
}
