package com.example.userauthentication.exception;

import com.example.userauthentication.exception.auth.JWTAuthenticationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        System.out.printf("Known error %s: %s", ex.getClass().getSimpleName(), ex);

        return new ResponseEntity<>("Not authorized!", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        System.out.printf("ERRO DEFAULT %s: %s", ex.getClass().getSimpleName(), ex);

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
