package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.dto.UserDTO;
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
    public ResponseEntity<?> getUsers(
            @RequestParam String username
    ) {
        UserDetails user = userService.loadUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable String id
    ) {
        //TODO: implement

        return null;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        var user = userService.createUser(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
