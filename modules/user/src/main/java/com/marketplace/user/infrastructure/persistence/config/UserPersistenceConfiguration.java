package com.marketplace.user.infrastructure.persistence.config;

import com.marketplace.user.infrastructure.persistence.entity.UserProfileEntity;
import com.marketplace.user.infrastructure.persistence.repository.UserProfileJpaRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = UserProfileEntity.class)
@EnableJpaRepositories(basePackageClasses = UserProfileJpaRepository.class)
public class UserPersistenceConfiguration { }
