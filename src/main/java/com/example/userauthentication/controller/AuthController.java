package com.example.userauthentication.controller;

import com.example.userauthentication.dto.LoginResponseDTO;
import com.example.userauthentication.service.UserService;
import com.example.userauthentication.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v0/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public LoginResponseDTO login(Authentication authentication) {
        log.info("Init user authentication: {}", authentication.getName());
        var loginResponse = authService.createLoginInfo(authentication);

        log.info("User successfully Authenticated: {}", loginResponse);
        return loginResponse;
    }
}
