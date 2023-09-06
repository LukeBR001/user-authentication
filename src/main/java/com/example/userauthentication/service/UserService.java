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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
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

    public List<UserDTO> loadUsers(Authentication authentication) {
        var userModel = getPrincipalUser(authentication);
        var bellowRoleNames = userModel.getBellowRoles().stream().map(Role::name).toList();

        return getUsersByRoles(bellowRoleNames).stream()
                .map(UserDTO::fromModel)
                .toList();
    }

    private UserModel getPrincipalUser(Authentication authentication) {
       return (UserModel) loadUserByUsername(authentication.getName());
    }

    private List<UserModel> getUsersByRoles(List<String> roles) {
        return userRepository.findAllByRoleIn(roles).stream()
                .map(UserModel::FromEntity)
                .toList();
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        var oldUser = loadUserByUsername(createUserDTO.username());
        if (oldUser != null) {
            throw new UserAlreadyExistsException("user already exists");
        }

        var userModel = buildUserModelToCreate(createUserDTO);
        userRepository.save(UserEntity.fromModel(userModel));

        return UserDTO.fromModel(userModel);
    }

    private UserModel buildUserModelToCreate(CreateUserDTO createUserDTO) {
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
