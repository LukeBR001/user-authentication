package com.example.userauthentication.service;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.exception.business.UserAlreadyExistsException;
import com.example.userauthentication.models.Role;
import com.example.userauthentication.models.Status;
import com.example.userauthentication.models.UserModel;
import com.example.userauthentication.repository.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserModel::FromEntity)
                .orElse(null);
//                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        var oldUser = loadUserByUsername(createUserDTO.username());
        if (oldUser != null){
            throw new UserAlreadyExistsException("user already exists");
        }

        var userModel = buildUserModelToCreate(createUserDTO);
        userRepository.save(UserEntity.fromModel(userModel));

        return UserDTO.fromModel(userModel);
    }

    private UserModel buildUserModelToCreate(CreateUserDTO createUserDTO){
        var encryptPass = passwordEncoder.encode(createUserDTO.password());
        return new UserModel(
                UUID.randomUUID().toString(),
                createUserDTO.username(),
                encryptPass,
                createUserDTO.description(),
                Status.ACTIVE,
                Role.getByName(createUserDTO.role())
        );

    }
}
