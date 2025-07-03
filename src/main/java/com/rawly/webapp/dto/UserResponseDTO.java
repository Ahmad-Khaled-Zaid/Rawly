package com.rawly.webapp.dto;

import java.util.UUID;

import com.rawly.webapp.domain.Gender;

public record UserResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        Gender gender) {
}
