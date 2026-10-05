package com.marketplace.identity.infrastructure.security;

import java.time.Duration;
import java.util.Objects;

public record JwtProperties(
        String secretKey,
        Duration accessTokenTtl,
        Duration refreshTokenTtl
) {
    public JwtProperties {
        Objects.requireNonNull(secretKey, "secret must not be null");
        Objects.requireNonNull(accessTokenTtl, "accessTokenTtl must not be null");
        Objects.requireNonNull(refreshTokenTtl, "refreshTokenTtl must not be null");

        if (secretKey.isBlank()) {
            throw new IllegalArgumentException("secret must not be blank");
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
