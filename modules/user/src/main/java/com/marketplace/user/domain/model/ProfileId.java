package com.marketplace.user.domain.model;

import java.util.Objects;
import java.util.UUID;

public record ProfileId(UUID value) {
    public ProfileId {
        Objects.requireNonNull(value, "profile id must not be null");
    }

    public static ProfileId generate() {
        return new ProfileId(UUID.randomUUID());
    }
}
