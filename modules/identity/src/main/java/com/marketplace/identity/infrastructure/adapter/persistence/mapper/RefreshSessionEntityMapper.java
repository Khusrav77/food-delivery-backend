package com.marketplace.identity.infrastructure.adapter.persistence.mapper;

import com.marketplace.identity.domain.model.RefreshSession;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.infrastructure.adapter.persistence.entity.RefreshSessionEntity;

import java.util.Objects;

public final class RefreshSessionEntityMapper {

    private RefreshSessionEntityMapper() {
    }

    public static RefreshSession toDomain(RefreshSessionEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return RefreshSession.reconstitute(
                entity.getId(),
                new UserId(entity.getUserAccountId()),
                entity.getCurrentTokenHash(),
                entity.getExpiresAt(),
                entity.getRevokedAt());
    }

    public static RefreshSessionEntity toEntity(RefreshSession session) {
        Objects.requireNonNull(session, "session must not be null");
        return new RefreshSessionEntity(
                session.id(),
                session.userId().value(),
                session.currentTokenHash(),
                session.expiresAt(),
                session.revokedAt());
    }
}
