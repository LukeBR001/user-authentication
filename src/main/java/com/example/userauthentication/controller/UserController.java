package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser(
            @RequestParam String username
    ) {
        UserDetails user = userService.loadUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        UserDetails oldUser = userService.loadUserByUsername(createUserDTO.username());
        if (oldUser != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        var user = userService.createUser(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
//
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> getUsers() {
//        var users = authService.loadUsers();
//        return ResponseEntity.ok(users);
//    }

}
