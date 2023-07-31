package com.example.userauthentication.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public static List<SimpleGrantedAuthority> getGrantedRoles(Role role) {
        if (role.equals(ADMIN)){
            return List.of(
                    new SimpleGrantedAuthority(ADMIN.name()),
                    new SimpleGrantedAuthority(USER.name())
                    );
        }

        if (role.equals(USER)){
            return List.of(
                    new SimpleGrantedAuthority(USER.name())
            );
        }
        return null;
    }
}
