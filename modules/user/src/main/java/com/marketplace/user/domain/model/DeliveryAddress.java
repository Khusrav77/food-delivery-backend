package com.marketplace.user.domain.model;

import java.util.Objects;

public record DeliveryAddress(
        DeliveryAddressId id,
        DeliveryAddressDetails details,
        boolean defaultAddress
) {
    public DeliveryAddress {
        Objects.requireNonNull(id, "address id must not be null");
        Objects.requireNonNull(details, "address details must not be null");
    }

    public DeliveryAddress update(DeliveryAddressDetails updatedDetails) {
        return new DeliveryAddress(id, updatedDetails, defaultAddress);
    }

    public DeliveryAddress withDefault(boolean isDefault) {
        return new DeliveryAddress(id, details, isDefault);
    }
}
