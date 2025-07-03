package com.rawly.webapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rawly.webapp.domain.model.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
}
