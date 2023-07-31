package com.example.userauthentication.controller;

import com.example.userauthentication.domain.Role;
import com.example.userauthentication.repository.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @GetMapping
    public UserEntity getUser() {
        var user = new UserEntity();
        user.setUsername("meu username");
        user.setRole(Role.USER);
        return user;
    }

}
