package com.rawly.webapp.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rawly.webapp.domain.model.User;
import com.rawly.webapp.exception.ResourceNotFoundException;
import com.rawly.webapp.domain.port.UserPersistencePort;
import com.rawly.webapp.security.UserDetailsAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        User user = userPersistencePort.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return new UserDetailsAdapter(user);
    }

    public UserDetails loadUserByUserId(UUID id) throws ResourceNotFoundException {
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserDetailsAdapter(user);
    }
}
