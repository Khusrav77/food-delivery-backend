package com.marketplace.identity.infrastructure.adapter.persistence.repository;

import com.marketplace.identity.infrastructure.adapter.persistence.entity.RefreshTokenHistoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public interface RefreshTokenHistoryJpaRepository
        extends JpaRepository<RefreshTokenHistoryEntity, String> {

    @Modifying
    @Transactional
    @Query("delete from RefreshTokenHistoryEntity h where h.expiresAt <= :now")
    int deleteExpiredAt(@Param("now") Instant now);
}
