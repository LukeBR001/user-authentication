package com.example.userauthentication.repository;

import com.example.userauthentication.domain.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Table(name = "users")
@Entity(name = "users")
@Data
@Setter
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;

    private String password;

    private String status;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.getGrantedRoles(role);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
        return true;
    }
}
