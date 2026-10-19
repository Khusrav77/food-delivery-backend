package com.marketplace.identity.infrastructure.adapter.persistence.mapper;


import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.infrastructure.adapter.persistence.entity.UserAccountEntity;
import java.util.Objects;


public final class UserAccountEntityMapper {

    private UserAccountEntityMapper() {
    }

    public static UserAccountEntity toEntity(UserAccount account) {
        Objects.requireNonNull(account, "account must not be null");

        return new UserAccountEntity(
                account.id().value(),
                account.phone().value(),
                account.status(),
                account.roles());
    }

    public static UserAccount toDomain(UserAccountEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        return UserAccount.reconstitute(
                new UserId(entity.getId()),
                new PhoneNumber(entity.getPhone()),
                entity.getRoles(),
                entity.getStatus());
    }
}
