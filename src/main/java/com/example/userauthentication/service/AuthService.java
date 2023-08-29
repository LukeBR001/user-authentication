package com.example.userauthentication.service;

import com.example.userauthentication.dto.LoginResponseDTO;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JWTProvider jwtProvider;

    public LoginResponseDTO createLoginInfo(Authentication authentication) {
        var user = (UserModel)authentication.getPrincipal();

        var token = jwtProvider.createToken(authentication);

        return buildResponseLoginInfo(user, token);
    }

    private LoginResponseDTO buildResponseLoginInfo(UserModel user, String token) {
        var userDTO = new UserDTO(
                user.aggregateId(),
                user.getUsername(),
                user.description(),
                user.status().name(),
                user.role().name()
        );

        return new LoginResponseDTO(userDTO, token);
    }
}