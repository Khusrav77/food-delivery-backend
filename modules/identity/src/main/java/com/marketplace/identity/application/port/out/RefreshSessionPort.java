package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.UserId;

import java.time.Instant;

public interface RefreshSessionPort {

    void start(UserId userId, String refreshToken, Instant expiresAt, Instant now);

    RotationResult rotate(
            UserId userId,
            String presentedToken,
            String replacementToken,
            Instant replacementExpiresAt,
            Instant now);

    void revokeCurrent(UserId userId, String refreshToken, Instant now);

    void revokeAll(UserId userId, Instant now);

    enum RotationResult {
        ROTATED,
        REUSED,
        INACTIVE,
        NOT_FOUND
    }
}
