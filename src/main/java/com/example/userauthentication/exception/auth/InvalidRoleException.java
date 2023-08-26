package com.example.userauthentication.exception.auth;

public class InvalidRoleException extends RuntimeException{
    public InvalidRoleException(String message){
        super(message);
    }
}
