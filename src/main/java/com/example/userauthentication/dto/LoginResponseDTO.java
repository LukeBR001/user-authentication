package com.example.userauthentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
        @JsonProperty("user") UserDTO userDTO,
        String token) {}
