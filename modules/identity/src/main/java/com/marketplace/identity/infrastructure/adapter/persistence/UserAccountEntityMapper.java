package com.marketplace.identity.infrastructure.adapter.persistence;

import com.marketplace.identity.domain.model.AccountStatus;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.Role;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserAccountEntityMapper {

    private UserAccountEntityMapper() {
    }

    public static UserAccountEntity toEntity(UserAccount account) {
        Set<String> roles = account.roles().stream()
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());
        return new UserAccountEntity(
                account.id().value(),
                account.phone().value(),
                roles,
                account.status().name());
    }

    public static UserAccount toDomain(UserAccountEntity entity) {
        Set<Role> roles = entity.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toUnmodifiableSet());
        return UserAccount.reconstitute(
                new UserId(entity.getId()),
                new PhoneNumber(entity.getPhone()),
                roles,
                AccountStatus.valueOf(entity.getStatus()));
    }
}
