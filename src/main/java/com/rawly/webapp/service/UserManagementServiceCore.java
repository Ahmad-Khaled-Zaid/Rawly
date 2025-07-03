package com.rawly.webapp.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rawly.webapp.domain.model.Role;
import com.rawly.webapp.domain.model.User;
import com.rawly.webapp.domain.port.UserPersistencePort;
import com.rawly.webapp.dto.UserCreateDTO;
import com.rawly.webapp.dto.UserResponseDTO;
import com.rawly.webapp.dto.UserUpdateDTO;
import com.rawly.webapp.exception.ResourceNotFoundException;
import com.rawly.webapp.exception.UserCreationException;
import com.rawly.webapp.mapper.UserMapper;
import com.rawly.webapp.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserManagementServiceCore implements UserManagementService {

    private final UserPersistencePort userPersistencePort;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userDetails) {
        try {
            log.debug("Encoding password for user: {}", userDetails.getEmail());
            String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
            Set<Role> assignmentRoles = assignRoles(userDetails.getRoles());
            log.debug("Assigned roles: {}", userDetails.getRoles());
            User user = User.createFromDTO(userDetails, encodedPassword, assignmentRoles);
            log.debug("Saving user to DB...");
            User savedUser = userPersistencePort.save(user);
            userPersistencePort.flush();
            return UserMapper.toDto(savedUser);
        } catch (Exception ex) {
            log.error("Error while creating the user: ", ex);
            throw new UserCreationException("Failed to create user.", ex);
        }
    }

    public UserResponseDTO getUserById(UUID id) {
        log.info("Fetching user with ID: {}", id);
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new ResourceNotFoundException("User with ID " + id + " not found");
                });

        return UserMapper.toDto(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userPersistencePort.findAll();
        return UserMapper.toDtoList(users);
    }

    @Transactional
    public void updateUser(UUID id, UserUpdateDTO userDetails) {
        log.debug("Updating user with ID: {}", id);
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new ResourceNotFoundException("User with ID " + id + " not found");
                });

        user.updateFromDTO(userDetails);
        userPersistencePort.save(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        userPersistencePort.deleteById(id);
    }

    @Transactional
    public void deleteAllUsers() {
        userPersistencePort.deleteAll();
    }

    private Set<Role> assignRoles(List<String> rolesList) {
        return roleRepository.findByNameIn(rolesList);
    }

}
