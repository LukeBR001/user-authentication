package com.example.userauthentication.service;

import com.example.userauthentication.dto.CreateUserRequest;
import com.example.userauthentication.dto.UserDTO;
import com.example.userauthentication.exception.business.UserAlreadyExistsException;
import com.example.userauthentication.exception.business.UserNotFoundException;
import com.example.userauthentication.exception.business.UserWithoutPermission;
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
        var principalUser = getPrincipalUser(authentication);
        var bellowRoleNames = principalUser.getBellowRoles().stream().map(Role::name).toList();

        return getUsersByRoles(bellowRoleNames).stream()
                .map(UserDTO::fromModel)
                .toList();
    }

    public UserDTO loadUser(String aggregateId, Authentication authentication) {
        var principalRoles = getPrincipalUser(authentication).getBellowRoles();
        var wantedUser = getUserByAggregateId(aggregateId);

        validateRoles(principalRoles, wantedUser.role());

        return UserDTO.fromModel(wantedUser);
    }

    public UserDTO createUser(CreateUserRequest createUserRequest, Authentication authentication) {
        validateCreateUserRequest(createUserRequest, authentication);
        var userModel = buildUserModelToCreate(createUserRequest);
        var createdUser = createUser(userModel);
        return UserDTO.fromModel(createdUser);
    }

    private UserModel createUser(UserModel userModel) {
        var savedEntity = userRepository.save(UserEntity.fromModel(userModel));
        return UserModel.FromEntity(savedEntity);
    }

    public void deleteUser(String aggregateId, Authentication authentication) {
        var principalRoles = getPrincipalUser(authentication).getBellowRoles();
        var userToDelete = getUserByAggregateId(aggregateId);

        validateRoles(principalRoles, userToDelete.role());

        deleteUser(userToDelete);
    }

    private void deleteUser(UserModel userToDelete) {
        var updatedUser = UserModel.buildDeletedUser(userToDelete);

        var oldUser = userRepository.findByAggregateId(updatedUser.aggregateId()).get();
        var newUser = new UserEntity(
                oldUser.getId(),
                oldUser.getAggregateId(),
                updatedUser.username(),
                updatedUser.password(),
                updatedUser.description(),
                updatedUser.status().name(),
                updatedUser.role().name()
        );
        userRepository.saveAndFlush(newUser);
    }

    private void validateCreateUserRequest(CreateUserRequest createUserRequest, Authentication authentication) {
        var principalRoles = getPrincipalUser(authentication).getBellowRoles();
        var userRoleRequest = Role.valueOf(createUserRequest.role());
        validateRoles(principalRoles, userRoleRequest);

        var oldUser = loadUserByUsername(createUserRequest.username());
        if (oldUser != null) {
            throw new UserAlreadyExistsException("user already exists");
        }
    }

    private UserModel getUserByAggregateId(String aggregateId) {
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

    private static void validateRoles(List<Role> principalRoles, Role wantedUserRole) {
        if (!principalRoles.contains(wantedUserRole)) {
            throw new UserWithoutPermission("It is not possible to manipulate a user with a role equal to or greater than yours");
        }
    }
}
