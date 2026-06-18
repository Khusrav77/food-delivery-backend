package com.shh.foodeliverybackendapp.modules.address.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UserAddressResponse(
        UUID id,
        String title,
        String city,
        String street,
        String house,
        String apartment,
        String entrance,
        String floor,
        String intercom,
        String comment,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean isDefault,
        Instant createdAt,
        Instant updatedAt
) {}
