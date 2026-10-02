package com.marketplace.identity.application.result;

import com.marketplace.identity.domain.model.UserId;

import java.util.Objects;

public record VerifyOtpResult(
        UserId id
) {

    public VerifyOtpResult {
        Objects.requireNonNull(id, "account must not be null");
    }
}