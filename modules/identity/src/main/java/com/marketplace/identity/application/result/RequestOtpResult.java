package com.marketplace.identity.application.result;

import java.util.Objects;

public record RequestOtpResult(
        long expiresInSeconds
) {

    public RequestOtpResult {
        Objects.requireNonNull(expiresInSeconds, "expiresInSeconds is null");
    }
}