package com.marketplace.user.domain.model;

import java.util.Objects;
import java.util.UUID;

public record DeliveryAddressId(UUID value) {
    public DeliveryAddressId {
        Objects.requireNonNull(value, "delivery address id must not be null");
    }

    public static DeliveryAddressId generate() {
        return new DeliveryAddressId(UUID.randomUUID());
    }
}
