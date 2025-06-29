package com.rawly.webapp.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import com.rawly.webapp.dto.auth.LoginRequest;
import com.rawly.webapp.dto.auth.LoginResponse;
import com.rawly.webapp.security.IAuthenticatedUser;
import com.rawly.webapp.security.IUserDetailsWithId;
import com.rawly.webapp.security.JwtService;

import jakarta.security.auth.message.AuthStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public LoginResponse login(LoginRequest request) {

        IAuthenticatedUser user = authenticatedUserService.loadUserByUserEmail(request.getEmail()); // prevent enumeration attacks

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!user.isEnabled()) {
            log.warn("Login attempt for disabled account: {}", request.getEmail());
            throw new BadCredentialsException("Account is disabled. Please contact support.");
        }

        if (!user.isAccountNonLocked()) {
            log.warn("Login attempt for locked account: {}", request.getEmail());
            throw new BadCredentialsException("Account is locked. Please try again later or reset your password.");
        }

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            if (!(userDetails instanceof IUserDetailsWithId)) {
                log.error("Unexpected user type: {} for email: {}", userDetails.getClass().getName(),
                        request.getEmail());
                throw new ServerErrorException("Authentication error: Invalid user type", null);
            }
            IUserDetailsWithId rawlyUser = (IUserDetailsWithId) userDetails;
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            log.info("User logged in successfully: {}", request.getEmail());
            return new LoginResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    rawlyUser.getUserId(),
                    userDetails.getUsername(),
                    AuthStatus.SUCCESS,
                    "Login successful");
        } catch (BadCredentialsException ex) {
            log.warn("Authentication failed for user: {} - {}", request.getEmail(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during login for user: {} ", request.getEmail(), ex);
            throw new ServerErrorException(
                    "An unexpected error occurred during authentication. Please try again later.", ex);
        }
    }
}
