package com.marketplace.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeliveryAddressRequest(
        @Size(max = 80) String label,
        @NotBlank @Size(max = 120) String city,
        @NotBlank @Size(max = 180) String street,
        @NotBlank @Size(max = 40) String building,
        @Size(max = 40) String apartment,
        @Size(max = 40) String entrance,
        @Size(max = 40) String floor,
        @Size(max = 40) String intercom,
        @Size(max = 500) String comment,
        boolean defaultAddress
) { }
