package com.marketplace.user.domain.model;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {
    public AccountId {
        Objects.requireNonNull(value, "account id must not be null");
    }
}
