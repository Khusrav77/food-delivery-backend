package com.marketplace.identity.infrastructure.adapter.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "identity_refresh_token_history")
public class RefreshTokenHistoryEntity {

    @Id
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "session_id", nullable = false, updatable = false)
    private UUID sessionId;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    protected RefreshTokenHistoryEntity() {
        // Required by JPA
    }

    public RefreshTokenHistoryEntity(
            String tokenHash,
            UUID sessionId,
            Instant consumedAt,
            Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.sessionId = sessionId;
        this.consumedAt = consumedAt;
        this.expiresAt = expiresAt;
    }

    public String getTokenHash() { return tokenHash; }
    public UUID getSessionId() { return sessionId; }
    public Instant getConsumedAt() { return consumedAt; }
    public Instant getExpiresAt() { return expiresAt; }

    public void consume(Instant now) {
        this.consumedAt = now;
    }
}
