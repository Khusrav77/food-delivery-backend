package com.marketplace.identity.application;

import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;

public record RequestOtpResult(
        UserAccount account,
        OtpCode otp
) {

    public RequestOtpResult {
        Objects.requireNonNull(account, "account must not be null");
        Objects.requireNonNull(otp, "otp must not be null");
    }
}