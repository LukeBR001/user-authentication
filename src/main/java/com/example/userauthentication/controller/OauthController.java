package com.example.userauthentication.controller;

import com.example.userauthentication.service.AuthService;
import com.example.userauthentication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class OauthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserRepository repository;
//
//    @Autowired
//    private TokenService tokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(Authentication authentication) {
        LOGGER.info("Init user authentication: {}", authentication.getName());
        var loginResponse = authService.createLoginInfo(authentication);

        LOGGER.info("Authenticated user: {}", loginResponse);


//        var auth = authenticationManager.authenticate(usernamePassword);
//        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());
        return ResponseEntity.ok(loginResponse);
    }
}
