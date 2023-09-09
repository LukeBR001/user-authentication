package com.example.userauthentication.service;

import com.example.userauthentication.dto.CreateUserRequest;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.exception.business.CreateUserException;
import com.example.userauthentication.exception.business.UserAlreadyExistsException;
import com.example.userauthentication.exception.business.UserNotFoundException;
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

    public UserDTO loadUser(String aggregateId, Authentication authentication) {
        var principalRoles = getPrincipalUser(authentication).getBellowRoles();
        var wantedUser = getUser(aggregateId);

        if (!principalContainsInBellowRole(principalRoles, wantedUser.role())) {
            throw new UserNotFoundException("User not found or no has bellow role");
        }

        return UserDTO.fromModel(wantedUser);
    }

    public UserDTO createUser(CreateUserRequest createUserRequest, Authentication authentication) {
        validateCreateUserRequest(createUserRequest, authentication);

        var userModel = buildUserModelToCreate(createUserRequest);
        userRepository.save(UserEntity.fromModel(userModel));

        return UserDTO.fromModel(userModel);
    }

    private void validateCreateUserRequest(CreateUserRequest createUserRequest, Authentication authentication) {
        var principalRoles = getPrincipalUser(authentication).getBellowRoles();
        if (!principalContainsInBellowRole(principalRoles, Role.valueOf(createUserRequest.role()))) {
            throw new CreateUserException("trying to create a user with a role equal to or greater than yours");
        }

        var oldUser = loadUserByUsername(createUserRequest.username());
        if (oldUser != null) {
            throw new UserAlreadyExistsException("user already exists");
        }
    }

    private UserModel getUser(String aggregateId) {
        var userEntity = userRepository.findByAggregateId(aggregateId)
                .orElseThrow(() -> new UserNotFoundException("User not found by aggregateId: " + aggregateId));
        return UserModel.FromEntity(userEntity);
    }

    private UserModel getPrincipalUser(Authentication authentication) {
        return (UserModel) loadUserByUsername(authentication.getName());
    }

    private List<UserModel> getUsersByRoles(List<String> roles) {
        return userRepository.findAllByRoleIn(roles).stream()
                .map(UserModel::FromEntity)
                .toList();
    }

    private UserModel buildUserModelToCreate(CreateUserRequest createUserRequest) {
        var encryptPass = passwordEncoder.encode(createUserRequest.password());
        return new UserModel(
                UUID.randomUUID().toString(),
                createUserRequest.username(),
                encryptPass,
                createUserRequest.description(),
                Status.ACTIVE,
                Role.getByName(createUserRequest.role())
        );

    }

    private static boolean principalContainsInBellowRole(List<Role> principalRoles, Role wantedUserRole) {
        return principalRoles.contains(wantedUserRole);
    }
}
