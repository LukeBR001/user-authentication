package com.example.userauthentication.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ResponseError> handleCredentialsException(AuthenticationException ex, HttpServletRequest request) {
        System.out.printf("Known error %s: %s", ex.getClass().getSimpleName(), ex);

        return buildResponseEntity(
                "username or password is invalid",
                request,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        System.out.printf("Known error %s: %s", ex.getClass().getSimpleName(), ex);

        return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
        System.out.printf("Unknown error %s: %s", ex.getClass().getSimpleName(), ex);

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
