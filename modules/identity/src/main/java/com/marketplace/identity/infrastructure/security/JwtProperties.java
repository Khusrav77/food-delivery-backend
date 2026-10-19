package com.marketplace.identity.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String activeKeyId,
        Map<String, String> keys,
        Duration accessTokenTtl,
        Duration refreshTokenTtl
) {
    public JwtProperties {
        Objects.requireNonNull(activeKeyId, "activeKeyId must not be null");
        Objects.requireNonNull(keys, "keys must not be null");
        Objects.requireNonNull(accessTokenTtl, "accessTokenTtl must not be null");
        Objects.requireNonNull(refreshTokenTtl, "refreshTokenTtl must not be null");

        if (activeKeyId.isBlank()) {
            throw new IllegalArgumentException("activeKeyId must not be blank");
        }

        keys = Map.copyOf(keys);
        if (keys.isEmpty() || !keys.containsKey(activeKeyId)) {
            throw new IllegalArgumentException(
                    "keys must contain the activeKeyId");
        }
        keys.forEach((keyId, secret) -> {
            if (keyId == null || keyId.isBlank()) {
                throw new IllegalArgumentException("key id must not be blank");
            }
            JwtKeyProvider.createSigningKey(secret);
        });

        if (keys.values().stream().distinct().count() != keys.size()) {
            throw new IllegalArgumentException("JWT keys must be unique");
        }

        if (accessTokenTtl.isZero() || accessTokenTtl.isNegative()) {
            throw new IllegalArgumentException(
                    "accessTokenTtl must be greater than zero");
        }

        if (refreshTokenTtl.isZero() || refreshTokenTtl.isNegative()) {
            throw new IllegalArgumentException(
                    "refreshTokenTtl must be greater than zero");
        }
    }
}
