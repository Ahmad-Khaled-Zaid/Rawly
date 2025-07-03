package com.rawly.webapp.service;

import java.util.List;
import java.util.UUID;

import com.rawly.webapp.dto.UserCreateDTO;
import com.rawly.webapp.dto.UserResponseDTO;
import com.rawly.webapp.dto.UserUpdateDTO;

public interface UserManagementService {
    UserResponseDTO createUser(UserCreateDTO userDetails);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(UUID id);

    void updateUser(UUID id, UserUpdateDTO user);

    void deleteUserById(UUID id);

    void deleteAllUsers();

}
