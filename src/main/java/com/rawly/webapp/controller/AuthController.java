package com.rawly.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rawly.webapp.dto.auth.LoginRequest;
import com.rawly.webapp.dto.auth.LoginResponse;
import com.rawly.webapp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    // @PostMapping("/register")
    // public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
    //     RegisterResponse registerResponse = authService.register(request);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    // }
}
