package com.marketplace.identity.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class RefreshSession {

    private final UUID id;
    private final UserId userId;
    private String currentTokenHash;
    private Instant expiresAt;
    private Instant revokedAt;

    private RefreshSession(
            UUID id,
            UserId userId,
            String currentTokenHash,
            Instant expiresAt,
            Instant revokedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.currentTokenHash = requireText(currentTokenHash, "currentTokenHash");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        this.revokedAt = revokedAt;
    }

    public static RefreshSession start(
            UserId userId,
            String tokenHash,
            Instant expiresAt,
            Instant now) {
        Objects.requireNonNull(now, "now must not be null");
        if (!expiresAt.isAfter(now)) {
            throw new IllegalArgumentException("expiresAt must be in the future");
        }
        return new RefreshSession(UUID.randomUUID(), userId, tokenHash, expiresAt, null);
    }

    public static RefreshSession reconstitute(
            UUID id,
            UserId userId,
            String currentTokenHash,
            Instant expiresAt,
            Instant revokedAt) {
        return new RefreshSession(id, userId, currentTokenHash, expiresAt, revokedAt);
    }

    public Rotation rotate(
            String presentedTokenHash,
            String replacementTokenHash,
            Instant replacementExpiresAt,
            Instant now) {
        Objects.requireNonNull(now, "now must not be null");
        if (revokedAt != null || !expiresAt.isAfter(now)) {
            return Rotation.INACTIVE;
        }
        if (!currentTokenHash.equals(presentedTokenHash)) {
            revokedAt = now;
            return Rotation.REUSED;
        }
        if (!replacementExpiresAt.isAfter(now)) {
            throw new IllegalArgumentException("replacementExpiresAt must be in the future");
        }
        currentTokenHash = requireText(replacementTokenHash, "replacementTokenHash");
        expiresAt = replacementExpiresAt;
        return Rotation.ROTATED;
    }

    public void revoke(Instant now) {
        Objects.requireNonNull(now, "now must not be null");
        if (revokedAt == null) {
            revokedAt = now;
        }
    }

    public UUID id() {
        return id;
    }

    public UserId userId() {
        return userId;
    }

    public String currentTokenHash() {
        return currentTokenHash;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public Instant revokedAt() {
        return revokedAt;
    }

    public enum Rotation {
        ROTATED,
        REUSED,
        INACTIVE
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
