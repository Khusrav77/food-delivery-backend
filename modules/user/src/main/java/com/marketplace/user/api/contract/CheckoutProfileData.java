package com.marketplace.user.api.contract;

import java.util.List;
import java.util.UUID;

public record CheckoutProfileData(
        UUID accountId,
        String firstName,
        String lastName,
        String email,
        Integer yearOfBirth,
        String avatarUrl,
        List<DeliveryAddressData> addresses,
        boolean readyForCheckout
) {
    public CheckoutProfileData {
        addresses = List.copyOf(addresses);
    }
}
