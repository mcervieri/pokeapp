package com.pokeapp.web.controller;

import com.pokeapp.application.service.AuthService;
import com.pokeapp.web.dto.auth.AuthResponse;
import com.pokeapp.web.dto.auth.LoginRequest;
import com.pokeapp.web.dto.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(
                request.username(),
                request.email(),
                request.password());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse(token, request.username()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.username(), request.password());
        return ResponseEntity.ok(new AuthResponse(token, request.username()));
    }
}