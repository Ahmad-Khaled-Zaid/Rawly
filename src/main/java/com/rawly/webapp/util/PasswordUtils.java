package com.rawly.webapp.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordUtils {
    private final PasswordEncoder passwordEncoder;

    // public static String encodePassword(String password) {
    //     return passwordEncoder.encode(password);
    // }
}
