package com.rawly.webapp.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.rawly.webapp.domain.model.User;

public interface UserPersistencePort {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    List<User> findAll();

    User save(User user);

    void deleteById(UUID id);

    void deleteAll();

    void flush();

}
