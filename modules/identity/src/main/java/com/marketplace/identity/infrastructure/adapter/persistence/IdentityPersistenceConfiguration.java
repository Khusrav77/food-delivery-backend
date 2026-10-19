package com.marketplace.identity.infrastructure.adapter.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = UserAccountEntity.class)
@EnableJpaRepositories(basePackageClasses = UserAccountJpaRepository.class)
public class IdentityPersistenceConfiguration {
}
