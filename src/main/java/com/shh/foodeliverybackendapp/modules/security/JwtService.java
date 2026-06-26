package com.shh.foodeliverybackendapp.modules.security;

import com.shh.foodeliverybackendapp.modules.user.entity.Role;

import java.util.UUID;

public interface JwtService {
    String generateAccessToken(UUID userId, Role role);
    String generateRefreshToken(UUID userId);
    boolean isTokenValid(String token);
    boolean isAccessToken(String token);
    boolean isRefreshToken(String token);
    UUID extractUserId(String token);
    String extractRole(String token);
}
