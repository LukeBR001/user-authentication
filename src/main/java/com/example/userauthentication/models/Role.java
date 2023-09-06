package com.example.userauthentication.models;

import com.example.userauthentication.exception.auth.InvalidRoleException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Stream;

public enum Role {
    ADMIN(1),
    USER(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public static List<SimpleGrantedAuthority> getGrantedRoles(Role role) {
        if (role.equals(ADMIN)) {
            return List.of(
                    new SimpleGrantedAuthority(ADMIN.name()),
                    new SimpleGrantedAuthority(USER.name())
            );
        }

        if (role.equals(USER)) {
            return List.of(
                    new SimpleGrantedAuthority(USER.name())
            );
        }
        throw new InvalidRoleException("Invalid role: " + role.name());
    }

    public static Role getByName(String role) {
        return Stream.of(values())
                .filter(r -> r.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRoleException("Invalid role name: " + role));
    }

    public static Role getById(int id) {
        return Stream.of(values())
                .filter(r -> r.id == id)
                .findFirst()
                .orElseThrow(() -> new InvalidRoleException("Invalid role id: " + id));
    }
}
