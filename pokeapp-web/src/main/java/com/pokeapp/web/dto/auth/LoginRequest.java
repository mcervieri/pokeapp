package com.pokeapp.web.dto.auth;

public record LoginRequest(
        String username,
        String password) {
}