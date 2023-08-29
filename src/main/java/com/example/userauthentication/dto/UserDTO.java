package com.example.userauthentication.dto;

import com.example.userauthentication.models.UserModel;

public record UserDTO(String aggregateId,
                      String username,
                      String description,
                      String status,
                      String role) {
    public static UserDTO fromModel(UserModel userModel) {
        return new UserDTO(
                userModel.aggregateId(),
                userModel.getUsername(),
                userModel.description(),
                userModel.status().name(),
                userModel.role().name()
        );
    }
}
