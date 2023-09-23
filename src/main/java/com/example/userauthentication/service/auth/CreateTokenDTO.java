package com.example.userauthentication.service.auth;

import java.util.List;

public record CreateTokenDTO(String aggregateId,
                             String username,
                             String description,
                             String status,
                             String role) {
}
