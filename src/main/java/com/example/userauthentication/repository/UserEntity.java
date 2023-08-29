package com.example.userauthentication.repository;

import com.example.userauthentication.models.Role;
import com.example.userauthentication.models.Status;
import com.example.userauthentication.models.UserModel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private static final String SEQUENCE = "users_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    private Long id;

    private String aggregateId;

    private String username;

    private String password;

    private String description;

    private String status;

    private String role;

    public static UserEntity fromModel(UserModel userModel) {
        return UserEntity.builder()
                .aggregateId(userModel.aggregateId())
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .status(userModel.status().name())
                .role(userModel.role().name())
                .build();
    }
}

