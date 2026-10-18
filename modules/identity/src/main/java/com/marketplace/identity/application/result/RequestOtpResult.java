package com.marketplace.identity.application.result;

import java.util.Objects;

public record RequestOtpResult(
        long expiresInSeconds,
        String otpCode
) {

    public RequestOtpResult {
        Objects.requireNonNull(expiresInSeconds, "expiresInSeconds is null");
        Objects.requireNonNull(otpCode, "otpCode is null");
    }
}
