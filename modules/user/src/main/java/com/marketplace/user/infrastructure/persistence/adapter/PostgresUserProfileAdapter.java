package com.marketplace.user.infrastructure.persistence.adapter;

import com.marketplace.user.application.port.out.UserProfileRepository;
import com.marketplace.user.domain.model.AccountId;
import com.marketplace.user.domain.model.DeliveryAddress;
import com.marketplace.user.domain.model.DeliveryAddressDetails;
import com.marketplace.user.domain.model.DeliveryAddressId;
import com.marketplace.user.domain.model.ProfileDetails;
import com.marketplace.user.domain.model.ProfileId;
import com.marketplace.user.domain.model.UserProfile;
import com.marketplace.user.infrastructure.persistence.entity.DeliveryAddressEntity;
import com.marketplace.user.infrastructure.persistence.entity.UserProfileEntity;
import com.marketplace.user.infrastructure.persistence.repository.UserProfileJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresUserProfileAdapter implements UserProfileRepository {

    private final UserProfileJpaRepository repository;

    public PostgresUserProfileAdapter(UserProfileJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> findByAccountId(AccountId accountId) {
        return repository.findByAccountId(accountId.value()).map(this::toDomain);
    }

    @Override
    @Transactional
    public UserProfile save(UserProfile profile) {
        UserProfileEntity entity = repository.findByAccountId(profile.accountId().value())
                .orElseGet(() -> new UserProfileEntity(
                        profile.id().value(), profile.accountId().value(), Instant.now()));
        ProfileDetails details = profile.details();
        entity.updateProfileFields(details.firstName(), details.lastName(), details.email(),
                details.yearOfBirth(), details.avatarUrl(), Instant.now());
        synchronizeAddresses(entity, profile.addresses());
        return toDomain(repository.save(entity));
    }

    private void synchronizeAddresses(UserProfileEntity profile, List<DeliveryAddress> domainAddresses) {
        Map<UUID, DeliveryAddressEntity> existing = new HashMap<>();
        for (DeliveryAddressEntity address : profile.addresses()) {
            existing.put(address.id(), address);
        }

        List<UUID> retainedIds = domainAddresses.stream()
                .map(address -> address.id().value())
                .toList();
        profile.addresses().removeIf(address -> !retainedIds.contains(address.id()));

        for (DeliveryAddress address : domainAddresses) {
            DeliveryAddressEntity entity = existing.get(address.id().value());
            if (entity == null) {
                entity = new DeliveryAddressEntity(address.id().value(), profile);
                profile.addresses().add(entity);
            }
            DeliveryAddressDetails details = address.details();
            entity.update(profile, details.label(), details.city(), details.street(),
                    details.building(), details.apartment(), details.entrance(), details.floor(),
                    details.intercom(), details.comment(), address.defaultAddress());
        }
    }

    private UserProfile toDomain(UserProfileEntity entity) {
        ProfileDetails details = new ProfileDetails(entity.firstName(), entity.lastName(),
                entity.email(), entity.yearOfBirth(), entity.avatarUrl());
        List<DeliveryAddress> addresses = entity.addresses().stream()
                .map(this::toDomain)
                .toList();
        return UserProfile.reconstitute(
                new ProfileId(entity.id()),
                new AccountId(entity.accountId()),
                details,
                addresses);
    }

    private DeliveryAddress toDomain(DeliveryAddressEntity entity) {
        DeliveryAddressDetails details = new DeliveryAddressDetails(
                entity.label(), entity.city(), entity.street(), entity.building(),
                entity.apartment(), entity.entrance(), entity.floor(), entity.intercom(),
                entity.comment());
        return new DeliveryAddress(
                new DeliveryAddressId(entity.id()), details, entity.defaultAddress());
    }
}
