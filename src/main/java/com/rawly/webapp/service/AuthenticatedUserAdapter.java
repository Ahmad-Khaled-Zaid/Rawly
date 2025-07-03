package com.rawly.webapp.service;

import com.rawly.webapp.domain.model.User;
import com.rawly.webapp.security.IAuthenticatedUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticatedUserAdapter implements IAuthenticatedUser {

    private final User user;

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }
}
