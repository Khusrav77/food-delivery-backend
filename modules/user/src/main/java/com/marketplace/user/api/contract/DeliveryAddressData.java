package com.marketplace.user.api.contract;

import java.util.UUID;

public record DeliveryAddressData(
        UUID id,
        String label,
        String city,
        String street,
        String building,
        String apartment,
        String entrance,
        String floor,
        String intercom,
        String comment,
        boolean defaultAddress
) { }
