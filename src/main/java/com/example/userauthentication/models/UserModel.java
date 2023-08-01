package com.example.userauthentication.models;

import org.springframework.security.core.userdetails.UserDetails;

public record UserModel(String username,
                        String password) {

    public static UserModel fromEntity(UserDetails user) {
        return new UserModel(
                user.getUsername(),
                user.getPassword()
        );
    }
}