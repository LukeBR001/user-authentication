package com.example.userauthentication.exception.auth;

public class JWTGenerateTokenException extends RuntimeException{
    public JWTGenerateTokenException(String message, Throwable cause){
        super(message, cause);
    }
}
