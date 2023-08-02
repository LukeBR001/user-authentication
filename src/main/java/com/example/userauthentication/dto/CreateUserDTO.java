package com.example.userauthentication.dto;

public record CreateUserDTO(String username,
                            String password,
                            String role) {
}
