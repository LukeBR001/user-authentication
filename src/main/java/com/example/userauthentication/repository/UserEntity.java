package com.example.userauthentication.repository;

import com.example.userauthentication.models.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Table(name = "users")
@Entity(name = "users")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    private static final String SEQUENCE = "users_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    private Long id;

    private String username;

    private String password;

    private String status;

    private String role;

    public UserEntity(
            String username,
            String password,
            String role
    ){
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = "ACTIVE";
    }

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
