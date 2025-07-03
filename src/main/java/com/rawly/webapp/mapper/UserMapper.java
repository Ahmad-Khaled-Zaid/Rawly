package com.rawly.webapp.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.rawly.webapp.domain.model.User;
import com.rawly.webapp.dto.UserResponseDTO;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getGender());
    }

    public static List<UserResponseDTO> toDtoList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

}
