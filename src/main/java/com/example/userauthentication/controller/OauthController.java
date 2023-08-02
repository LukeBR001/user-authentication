package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.repository.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class OauthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO auth) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(auth.username(), auth.password());
        var responseAuth = authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        if (repository.findByUsername(createUserDTO.username()) != null){
            return null;
        }

        String encryptPass = new BCryptPasswordEncoder().encode(createUserDTO.password());
        UserEntity user = new UserEntity(
                createUserDTO.username(),
                encryptPass,
                createUserDTO.role()
        );

        repository.save(user);
        return user;
    }
}
