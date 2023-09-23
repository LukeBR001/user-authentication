package com.example.userauthentication.controller;

import com.example.userauthentication.dto.CreateUserRequest;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication
    ) {
        List<UserDTO> userDTOS = userService.loadUsers(authentication);
        log.info("Users successfully returned: {}", userDTOS);
        return userDTOS;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @PathVariable String id) {
        UserDTO userDTO = userService.loadUser(id, authentication);
        log.info("User successfully fetched: {}", userDTO);
        return userDTO;
    }

    @PostMapping
    public UserDTO createUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @RequestBody @Valid CreateUserRequest createUserRequest) {

        UserDTO userDTO = userService.createUser(createUserRequest, authentication);
        log.info("User successfully created: {}", userDTO);
        return userDTO;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @PathVariable String id) {
        userService.deleteUser(id, authentication);
        log.info("User {} successfully deleted", id);
    }
}
