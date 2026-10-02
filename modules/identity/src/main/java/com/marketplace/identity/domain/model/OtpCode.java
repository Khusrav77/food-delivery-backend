package com.marketplace.identity.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class OtpCode {

    private static final int MAX_ATTEMPTS = 5;
    private final String value;
    private final Instant expiresAt;
    private int attempts;
    private boolean used;

    private OtpCode(String value, Instant expiresAt) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
    }

    public static OtpCode create(String value, Instant expiresAt) {
        validateValue(value);
        Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        return new OtpCode(value, expiresAt);
    }

    public boolean verify(String candidate, Instant now) {
        Objects.requireNonNull(candidate, "candidate must not be null");
        Objects.requireNonNull(now, "now must not be null");

        if (used) {return false;}

        if (!now.isBefore(expiresAt)) {return false;}

        if (attempts >= MAX_ATTEMPTS) {return false;}

        attempts++;

        if (value.equals(candidate)) {
            used = true;
            return true;
        }

        return false;
    }

    public boolean isExpired(Instant now) {
        Objects.requireNonNull(now, "now must not be null");
        return !now.isBefore(expiresAt);
    }

    public boolean isUsed() {return used;}
    public int attempts() {return attempts;}
    public Instant expiresAt() {return expiresAt;}

    private static void validateValue(String value) {
        if (value == null || !value.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "OTP code must contain exactly 6 digits");
        }
    }
}