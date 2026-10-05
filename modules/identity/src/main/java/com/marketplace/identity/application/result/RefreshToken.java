package com.marketplace.identity.application.result;

import java.util.Objects;

public record RefreshToken(
        String value,
        long expiresInSeconds
) {
    public RefreshToken {
        Objects.requireNonNull(value, "value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }

        if (expiresInSeconds <= 0) {
            throw new IllegalArgumentException("expiresInSeconds must be greater than zero");
        }
    }
}