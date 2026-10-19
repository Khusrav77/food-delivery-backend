package com.marketplace.identity.infrastructure.adapter.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "identity_refresh_sessions")
public class RefreshSessionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_account_id", nullable = false, updatable = false)
    private UUID userAccountId;

    @Column(name = "current_token_hash", nullable = false, length = 64)
    private String currentTokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    protected RefreshSessionEntity() {
        // Required by JPA
    }

    public RefreshSessionEntity(
            UUID id,
            UUID userAccountId,
            String currentTokenHash,
            Instant expiresAt,
            Instant revokedAt) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.currentTokenHash = currentTokenHash;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
    }

    public UUID getId() { return id; }
    public UUID getUserAccountId() { return userAccountId; }
    public String getCurrentTokenHash() { return currentTokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }
}
