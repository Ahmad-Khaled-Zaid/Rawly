package com.rawly.webapp.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.rawly.webapp.model.User;
import com.rawly.webapp.repository.UserRepository;
import com.rawly.webapp.security.IAuthenticatedUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticatedUserService implements IAuthenticatedUserService {
    private final UserRepository userRepository;

    @Override
    public IAuthenticatedUser loadUserByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Login attempt failed - User not found with email: {}", email);
                    return new BadCredentialsException("Invalid email or password");
                });
        return new AuthenticatedUserAdapter(user);
    }
}
