package com.marketplace.identity.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, UUID> {
    Optional<UserAccountEntity> findByPhone(String phone);
}
