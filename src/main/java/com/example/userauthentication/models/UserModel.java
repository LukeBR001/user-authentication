package com.example.userauthentication.models;

import com.example.userauthentication.repository.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserModel(String aggregateId,
                        String username,
                        String password,
                        String description,
                        Status status,
                        Role role
)
        implements UserDetails {

    public static UserModel FromEntity(UserEntity userEntity) {
        return new UserModel(
                userEntity.getAggregateId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getDescription(),
                Status.valueOf(userEntity.getStatus()),
                Role.getByName(userEntity.getRole())
        );
    }

    public static UserModel buildDeletedUser(UserModel userToDelete) {
        return new UserModel(
                userToDelete.aggregateId(),
                userToDelete.username(),
                userToDelete.password(),
                userToDelete.description(),
                Status.DELETED,
                userToDelete.role()
        );
    }

    public List<Role> getBellowRoles() {
        return this.getAuthorities().stream()
                .filter(gr -> !gr.getAuthority().equals(role.name()))
                .map(r -> Role.valueOf(r.getAuthority()))
                .toList();
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