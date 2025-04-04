package com.sravani.movieapi.rest.dto;
//When a user logs in (/auth/authenticate), the backend generates a JWT token.
//The token is wrapped inside AuthResponse and returned:
public record AuthResponse(String accessToken) {
}
