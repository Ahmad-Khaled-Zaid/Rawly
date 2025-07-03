package com.rawly.webapp.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rawly.webapp.constants.AuthConstants;
import com.rawly.webapp.dto.auth.LoginRequest;
import com.rawly.webapp.dto.auth.LoginResponse;
import com.rawly.webapp.exception.AuthenticationServiceException;
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
    private final MessageSource messageSource;

    public LoginResponse login(LoginRequest request) {

        IAuthenticatedUser user = authenticatedUserService.loadUserByUserEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", request.getEmail());
            throw new BadCredentialsException(
                    messageSource.getMessage("auth.login.invalid-credentials", null,
                            AuthConstants.DEFAULT_LOGIN_ERROR,
                            LocaleContextHolder.getLocale()));
        }

        if (!user.isEnabled()) {
            log.warn("Login attempt for disabled account: {}", request.getEmail());
            throw new BadCredentialsException(
                    messageSource.getMessage("auth.login.account.disabled", null,
                            AuthConstants.DEFAULT_LOGIN_ERROR,
                            LocaleContextHolder.getLocale()));
        }

        if (!user.isAccountNonLocked()) {
            log.warn("Login attempt for locked account: {}", request.getEmail());
            throw new BadCredentialsException(
                    messageSource.getMessage("auth.login.account.locked", null,
                            AuthConstants.DEFAULT_LOGIN_ERROR,
                            LocaleContextHolder.getLocale()));
        }

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            if (!(userDetails instanceof IUserDetailsWithId)) {
                log.error("Unexpected user type: {} for email: {}", userDetails.getClass().getName(),
                        request.getEmail());
                throw new AuthenticationServiceException(
                        messageSource.getMessage("auth.login.unexpected-error", null,
                                AuthConstants.DEFAULT_LOGIN_ERROR, LocaleContextHolder.getLocale()));
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
                    messageSource.getMessage("auth.login.success", null,
                            "Login successful", LocaleContextHolder.getLocale()));
        } catch (BadCredentialsException ex) {
            log.warn("Authentication failed for user: {} - {}", request.getEmail(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during login for user: {} ", request.getEmail(), ex);
            throw new AuthenticationServiceException(
                    messageSource.getMessage("auth.login.unexpected-error", null,
                            AuthConstants.DEFAULT_LOGIN_ERROR, LocaleContextHolder.getLocale()));

        }
    }
}
