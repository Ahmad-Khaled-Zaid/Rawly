package com.rawly.webapp.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.rawly.webapp.domain.model.User;
import com.rawly.webapp.security.IAuthenticatedUser;
import com.rawly.webapp.domain.port.UserPersistencePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticatedUserService implements IAuthenticatedUserService {
    private final UserPersistencePort userPersistencePort;
    private final MessageSource messageSource;

    @Override
    public IAuthenticatedUser loadUserByUserEmail(String email) {
        User user = userPersistencePort.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Login attempt failed - User not found with email: {}", email);
                    return new BadCredentialsException(messageSource.getMessage("auth.login.invalid-credentials", null,
                            "An unexpected error occurred during login", LocaleContextHolder.getLocale()));
                });
        return new AuthenticatedUserAdapter(user);
    }
}
