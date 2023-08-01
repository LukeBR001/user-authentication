package com.example.userauthentication.controller;

import com.example.userauthentication.models.UserModel;
import com.example.userauthentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public UserModel getUser(
            @RequestParam String username
    ) {
        UserDetails user = authService.loadUserByUsername(username);
        System.out.println(user);
        return UserModel.fromEntity(user);
    }

}
