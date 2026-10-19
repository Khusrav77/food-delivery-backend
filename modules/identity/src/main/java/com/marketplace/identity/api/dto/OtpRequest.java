package com.marketplace.identity.api.dto;

import jakarta.validation.constraints.NotBlank;

public record OtpRequest(
        @NotBlank
        String phone
) {
}
