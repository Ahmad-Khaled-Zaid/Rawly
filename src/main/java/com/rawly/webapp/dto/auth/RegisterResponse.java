package com.rawly.webapp.dto.auth;

import java.util.UUID;

import jakarta.security.auth.message.AuthStatus;

public record RegisterResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UUID userId,
        String username,
        AuthStatus status,
        String message) {
}
