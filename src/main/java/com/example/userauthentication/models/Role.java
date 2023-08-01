package com.example.userauthentication.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static List<SimpleGrantedAuthority> getGrantedRoles(String role) {
        Role receivedRole = getByName(role);
        if (receivedRole.equals(ADMIN)){
            return List.of(
                    new SimpleGrantedAuthority(ADMIN.name()),
                    new SimpleGrantedAuthority(USER.name())
                    );
        }

        if (receivedRole.equals(USER)){
            return List.of(
                    new SimpleGrantedAuthority(USER.name())
            );
        }
        return null;
    }

    public static Role getByName(String role) {
        for (Role r: Role.values()) {
            if (r.role.equals(role)) {
                return r;
            }
        }
        throw new RuntimeException("Role nonexistent: " + role);
    }
}
