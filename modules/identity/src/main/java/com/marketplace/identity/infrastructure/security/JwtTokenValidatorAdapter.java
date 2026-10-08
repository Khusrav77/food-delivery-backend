package com.marketplace.identity.infrastructure.security;

import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.Role;
import com.marketplace.identity.domain.model.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class JwtTokenValidatorAdapter implements TokenValidatorPort {

    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private final JwtProperties properties;

    public JwtTokenValidatorAdapter(JwtProperties properties) {
        this.properties = Objects.requireNonNull(
                properties, "properties must not be null");
    }

    @Override
    public ValidatedToken validateAccessToken(String token) {
        return validateToken(token, ACCESS_TOKEN_TYPE);
    }

    @Override
    public ValidatedToken validateRefreshToken(String token) {
        return validateToken(token, REFRESH_TOKEN_TYPE);
    }

    private ValidatedToken validateToken(String token, String expectedTokenType) {
        Objects.requireNonNull(token, "token must not be null");

        if (token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(
                            JwtKeyProvider.createSigningKey(properties.secretKey()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            validateTokenType(claims, expectedTokenType);

            UserId userId = extractUserId(claims);
            Set<Role> roles = extractRoles(claims);

            return new ValidatedToken(userId, roles);

        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    private void validateTokenType(Claims claims, String expectedTokenType) {
        String actualTokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);

        if (!expectedTokenType.equals(actualTokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }
    }

    private UserId extractUserId(Claims claims) {
        String subject = claims.getSubject();

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("JWT subject must not be blank");
        }

        try {
            return new UserId(UUID.fromString(subject));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user id in JWT subject", e);
        }
    }

    private Set<Role> extractRoles(Claims claims) {

        List<String> roleNames = claims.get("roles", List.class);

        if (roleNames == null || roleNames.isEmpty()) {
            throw new IllegalArgumentException("JWT roles must not be empty");
        }

        try {
            return roleNames.stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toUnmodifiableSet());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role in JWT", e);
        }
    }
}