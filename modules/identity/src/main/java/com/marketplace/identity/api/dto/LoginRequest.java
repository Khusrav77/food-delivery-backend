package com.marketplace.identity.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String phone,
        @NotBlank
        String code
) {
}
