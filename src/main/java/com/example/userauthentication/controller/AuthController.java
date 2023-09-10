package com.example.userauthentication.controller;

import com.example.userauthentication.dto.LoginResponseDTO;
import com.example.userauthentication.service.UserService;
import com.example.userauthentication.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v0/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public LoginResponseDTO login(Authentication authentication) {
        LOGGER.info("Init user authentication: {}", authentication.getName());
        var loginResponse = authService.createLoginInfo(authentication);

        LOGGER.info("Authenticated user: {}", loginResponse);
        return loginResponse;
    }
}
