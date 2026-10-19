package com.marketplace.identity.infrastructure.adapter.persistence.repository;

import com.marketplace.identity.infrastructure.adapter.persistence.entity.RefreshSessionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshSessionJpaRepository extends JpaRepository<RefreshSessionEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from RefreshSessionEntity s where s.id = :id")
    Optional<RefreshSessionEntity> findByIdForUpdate(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("delete from RefreshSessionEntity s where s.expiresAt <= :now")
    int deleteExpiredAt(@Param("now") Instant now);

    @Modifying
    @Transactional
    @Query("update RefreshSessionEntity s set s.revokedAt = :now "
            + "where s.userAccountId = :userId and s.revokedAt is null")
    int revokeAllForUser(@Param("userId") UUID userId, @Param("now") Instant now);
}
