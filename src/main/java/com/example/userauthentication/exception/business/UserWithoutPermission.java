package com.example.userauthentication.exception.business;

public class UserWithoutPermission extends RuntimeException {
    public UserWithoutPermission(String message) {
        super(message);
    }
}
