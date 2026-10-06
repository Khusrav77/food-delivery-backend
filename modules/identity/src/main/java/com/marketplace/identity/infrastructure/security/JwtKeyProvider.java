package com.marketplace.identity.infrastructure.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public final class JwtKeyProvider {

    private JwtKeyProvider() {}

    public static SecretKey createSigningKey(String secret) {
        Objects.requireNonNull(secret, "secret must not be null");

        if (secret.isBlank()) {
            throw new IllegalArgumentException("secret must not be blank");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
