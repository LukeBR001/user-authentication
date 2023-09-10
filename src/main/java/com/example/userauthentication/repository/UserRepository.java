package com.example.userauthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByAggregateId(String aggregateId);
    List<UserEntity> findAllByRoleIn(List<String> roles);
}
