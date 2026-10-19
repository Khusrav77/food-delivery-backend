package com.marketplace.identity.infrastructure.adapter.persistence.config;

import com.marketplace.identity.infrastructure.adapter.persistence.entity.UserAccountEntity;
import com.marketplace.identity.infrastructure.adapter.persistence.repository.UserAccountJpaRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = UserAccountEntity.class)
@EnableJpaRepositories(basePackageClasses = UserAccountJpaRepository.class)
public class IdentityPersistenceConfiguration {
}
