package com.shh.foodeliverybackendapp.modules.address.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserAddressRequest(
        @Size(max = 50)
        String title,

        @NotBlank
        @Size(max = 100)
        String city,

        @NotBlank
        @Size(max = 150)
        String street,

        @NotBlank
        @Size(max = 20)
        String house,

        @Size(max = 20)
        String apartment,

        @Size(max = 20)
        String entrance,

        @Size(max = 10)
        String floor,

        @Size(max = 20)
        String intercom,

        @Size(max = 255)
        String comment,
        @DecimalMin("-90.000000")
        @DecimalMax("90.000000")
        BigDecimal latitude,

        @DecimalMin("-180.000000")
        @DecimalMax("180.000000")
        BigDecimal longitude,

        Boolean isDefault
        ) {}
