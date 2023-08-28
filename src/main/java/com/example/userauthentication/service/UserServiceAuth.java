package com.example.userauthentication.service;

import com.example.userauthentication.dto.CreateUserDTO;
import com.example.userauthentication.models.Role;
import com.example.userauthentication.models.UserModel;
import com.example.userauthentication.repository.UserEntity;
import com.example.userauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceAuth implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserModel::buildFromEntity)
                .orElse(null);
//                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    public List<UserEntity> loadUsers() throws UsernameNotFoundException {
        return userRepository.findAll();
    }

    public UserDetails createUser(CreateUserDTO createUserDTO) {
        String encryptPass = passwordEncoder.encode(createUserDTO.password());
        UserEntity userEntity = new UserEntity(
                createUserDTO.username(),
                encryptPass,
                Role.getByName(createUserDTO.role()).name()
        );
        return UserModel.buildFromEntity(userRepository.save(userEntity));

    }
}
