package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserRequest;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication
    ) {
        return userService.loadUsers(authentication);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @PathVariable String id) {
        return userService.loadUser(id, authentication);
    }

    @PostMapping
    public UserDTO createUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @RequestBody @Valid CreateUserRequest createUserRequest) {

        return userService.createUser(createUserRequest, authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @PathVariable String id) {
        userService.deleteUser(id, authentication);
    }
}
