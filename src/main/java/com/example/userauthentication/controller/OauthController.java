package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.dto.LoginResponseDTO;
import com.example.userauthentication.models.Role;
import com.example.userauthentication.repository.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import com.example.userauthentication.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        UserDetails oldUser = repository.findByUsername(createUserDTO.username());
        if (oldUser != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(createUserDTO.password());
        UserEntity user = new UserEntity(
                createUserDTO.username(),
                encryptPass,
                Role.getByName(createUserDTO.role()).name()
        );

        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
