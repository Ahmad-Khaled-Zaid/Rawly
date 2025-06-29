package com.rawly.webapp.security;

public interface IAuthenticatedUser {
    String getEmail();

    String getPassword();

    boolean isEnabled();

    boolean isAccountNonLocked();

}
