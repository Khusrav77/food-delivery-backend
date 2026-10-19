package com.marketplace.identity.infrastructure.adapter.persistence.adapter;

import com.marketplace.identity.application.port.out.RefreshSessionPort;
import com.marketplace.identity.domain.model.RefreshSession;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.infrastructure.adapter.persistence.entity.RefreshTokenHistoryEntity;
import com.marketplace.identity.infrastructure.adapter.persistence.mapper.RefreshSessionEntityMapper;
import com.marketplace.identity.infrastructure.adapter.persistence.repository.RefreshSessionJpaRepository;
import com.marketplace.identity.infrastructure.adapter.persistence.repository.RefreshTokenHistoryJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostgresRefreshSessionAdapter implements RefreshSessionPort {

    private final RefreshSessionJpaRepository sessions;
    private final RefreshTokenHistoryJpaRepository tokenHistory;

    public PostgresRefreshSessionAdapter(
            RefreshSessionJpaRepository sessions,
            RefreshTokenHistoryJpaRepository tokenHistory) {
        this.sessions = Objects.requireNonNull(sessions, "sessions must not be null");
        this.tokenHistory = Objects.requireNonNull(tokenHistory, "tokenHistory must not be null");
    }

    @Override
    @Transactional
    public void start(UserId userId, String refreshToken, Instant expiresAt, Instant now) {
        tokenHistory.deleteExpiredAt(now);
        sessions.deleteExpiredAt(now);
        String tokenHash = hash(refreshToken);
        RefreshSession session = RefreshSession.start(userId, tokenHash, expiresAt, now);
        sessions.save(RefreshSessionEntityMapper.toEntity(session));
        tokenHistory.save(new RefreshTokenHistoryEntity(
                tokenHash, session.id(), null, expiresAt));
    }

    @Override
    @Transactional
    public RotationResult rotate(
            UserId userId,
            String presentedToken,
            String replacementToken,
            Instant replacementExpiresAt,
            Instant now) {
        tokenHistory.deleteExpiredAt(now);
        sessions.deleteExpiredAt(now);
        String presentedHash = hash(presentedToken);
        Optional<RefreshTokenHistoryEntity> tokenRecord = tokenHistory.findById(presentedHash);
        if (tokenRecord.isEmpty()) {
            return RotationResult.NOT_FOUND;
        }

        RefreshTokenHistoryEntity history = tokenRecord.get();
        var sessionEntity = sessions.findByIdForUpdate(history.getSessionId());
        if (sessionEntity.isEmpty()) {
            return RotationResult.NOT_FOUND;
        }

        String replacementHash = hash(replacementToken);
        RefreshSession session = RefreshSessionEntityMapper.toDomain(sessionEntity.get());
        if (!session.userId().equals(userId)) {
            return RotationResult.NOT_FOUND;
        }
        RefreshSession.Rotation rotation = session.rotate(
                presentedHash,
                replacementHash,
                replacementExpiresAt,
                now);

        if (rotation == RefreshSession.Rotation.REUSED) {
            sessions.save(RefreshSessionEntityMapper.toEntity(session));
            return RotationResult.REUSED;
        }
        if (rotation == RefreshSession.Rotation.INACTIVE) {
            return RotationResult.INACTIVE;
        }

        history.consume(now);
        tokenHistory.save(history);
        tokenHistory.save(new RefreshTokenHistoryEntity(
                replacementHash, session.id(), null, replacementExpiresAt));
        sessions.save(RefreshSessionEntityMapper.toEntity(session));
        return RotationResult.ROTATED;
    }

    @Override
    @Transactional
    public void revokeCurrent(UserId userId, String refreshToken, Instant now) {
        tokenHistory.deleteExpiredAt(now);
        sessions.deleteExpiredAt(now);
        String tokenHash = hash(refreshToken);
        Optional<RefreshTokenHistoryEntity> tokenRecord = tokenHistory.findById(tokenHash);
        if (tokenRecord.isEmpty()) {
            return;
        }

        var sessionEntity = sessions.findByIdForUpdate(tokenRecord.get().getSessionId());
        if (sessionEntity.isEmpty()) {
            return;
        }

        RefreshSession session = RefreshSessionEntityMapper.toDomain(sessionEntity.get());
        if (session.userId().equals(userId)) {
            session.revoke(now);
            sessions.save(RefreshSessionEntityMapper.toEntity(session));
        }
    }

    @Override
    @Transactional
    public void revokeAll(UserId userId, Instant now) {
        tokenHistory.deleteExpiredAt(now);
        sessions.deleteExpiredAt(now);
        sessions.revokeAllForUser(userId.value(), now);
    }

    private static String hash(String token) {
        Objects.requireNonNull(token, "token must not be null");
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
