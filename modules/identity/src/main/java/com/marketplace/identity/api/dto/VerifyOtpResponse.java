package com.marketplace.identity.api.dto;

import java.util.UUID;

public record VerifyOtpResponse(
        UUID userId
) {
}
