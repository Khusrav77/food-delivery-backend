package com.marketplace.user.api.contract;

import java.util.UUID;

public record SaveCheckoutProfile(
        UUID accountId,
        String firstName,
        String lastName,
        String email,
        Integer yearOfBirth,
        String avatarUrl,
        DeliveryAddressInput newAddress,
        boolean makeAddressDefault
) {
    public record DeliveryAddressInput(
            String label,
            String city,
            String street,
            String building,
            String apartment,
            String entrance,
            String floor,
            String intercom,
            String comment
    ) { }
}
