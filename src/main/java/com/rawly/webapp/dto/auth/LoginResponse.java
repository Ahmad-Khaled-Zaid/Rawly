package com.rawly.webapp.dto.auth;

import java.util.UUID;

import jakarta.security.auth.message.AuthStatus;

public record LoginResponse(
                String accessToken,
                String refreshToken,
                String tokenType,
                UUID userId,
                String username,
                AuthStatus status,
                String message) {
}
