package com.marketplace.identity.application.result;

import com.marketplace.identity.domain.model.Role;
import com.marketplace.identity.domain.model.UserId;

import java.util.Objects;
import java.util.Set;

public record ValidatedToken(
        UserId userId,
        Set<Role> roles
) {

    public ValidatedToken {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(roles, "roles must not be null");
        roles = Set.copyOf(roles);
    }
}