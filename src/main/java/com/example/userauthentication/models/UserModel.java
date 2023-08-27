package com.example.userauthentication.models;

import com.example.userauthentication.repository.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record UserModel(Long id,
                        String username,
                        String password,
                        Status status,
                        Role role
)
        implements UserDetails {

    public static UserModel buildFromEntity(UserEntity userEntity) {
        return new UserModel(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                Status.valueOf(userEntity.getStatus()),
                Role.getByName(userEntity.getRole())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.getGrantedRoles(this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.isEnabled();
    }


}