package com.example.userauthentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@NotBlank String username,
                                @NotBlank String password,
                                @Size(max = 255) String description,
                                @NotBlank String role) {

}
