package com.marketplace.user.domain.model;

public final class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(DeliveryAddressId addressId) {
        super("Delivery address not found: " + addressId.value());
    }
}
