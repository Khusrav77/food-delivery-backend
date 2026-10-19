package com.marketplace.user.infrastructure.persistence.repository;

import com.marketplace.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, UUID> {
    @EntityGraph(attributePaths = "addresses")
    Optional<UserProfileEntity> findByAccountId(UUID accountId);
}
