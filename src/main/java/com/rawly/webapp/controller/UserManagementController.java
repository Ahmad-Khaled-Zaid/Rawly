package com.rawly.webapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rawly.webapp.dto.UserCreateDTO;
import com.rawly.webapp.dto.UserResponseDTO;
import com.rawly.webapp.dto.UserUpdateDTO;
import com.rawly.webapp.service.UserManagementService;
import com.rawly.webapp.validation.validationGroups.CreateGroup;
import com.rawly.webapp.validation.validationGroups.UpdateGroup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserManagementController {

    private final UserManagementService userManagementService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody @Validated(CreateGroup.class) UserCreateDTO userDetails) {
        log.debug("Received request to create user with email: {}", userDetails.getEmail());
        UserResponseDTO user = userManagementService.createUser(userDetails);
        log.debug("User created successfully with id: {}", user.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.debug("Received request to get all users");
        List<UserResponseDTO> users = userManagementService.getAllUsers();
        log.debug("Returning {} users", users.size());
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        log.debug("Received request to get user with id: {}", id);
        UserResponseDTO user = userManagementService.getUserById(id);
        log.debug("User retrieved: {}", user.id());
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id,
            @RequestBody @Validated(UpdateGroup.class) UserUpdateDTO user) {
        log.debug("Received request to update user with id: {}", id);
        userManagementService.updateUser(id, user);
        log.debug("User updated successfully with id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.debug("Received request to delete user with id: {}", id);
        userManagementService.deleteUserById(id);
        log.debug("User deleted successfully with id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        log.debug("Received request to delete all users");
        userManagementService.deleteAllUsers();
        log.debug("All users deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
