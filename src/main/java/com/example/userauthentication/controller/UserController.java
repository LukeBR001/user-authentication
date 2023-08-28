package com.example.userauthentication.controller;

import com.example.userauthentication.service.UserServiceAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @Autowired
    private UserServiceAuth userServiceAuth;

    @GetMapping
    public ResponseEntity<?> getUser(
            @RequestParam String username
    ) {
        UserDetails user = userServiceAuth.loadUserByUsername(username);

        return ResponseEntity.ok(user);
    }
//
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> getUsers() {
//        var users = authService.loadUsers();
//        return ResponseEntity.ok(users);
//    }

}
