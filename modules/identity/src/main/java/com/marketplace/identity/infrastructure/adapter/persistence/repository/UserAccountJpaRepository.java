package com.marketplace.identity.infrastructure.adapter.persistence.repository;

import com.marketplace.identity.infrastructure.adapter.persistence.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, UUID> {
    Optional<UserAccountEntity> findByPhone(String phone);
}
