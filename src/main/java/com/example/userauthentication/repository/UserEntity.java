package com.example.userauthentication.repository;

import com.example.userauthentication.models.Role;
import com.example.userauthentication.models.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
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
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = Status.ACTIVE.name();
    }
}
