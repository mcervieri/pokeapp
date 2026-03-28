package com.pokeapp.web.dto.auth;

public record AuthResponse(
        String token,
        String username) {
}