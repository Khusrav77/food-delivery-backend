package com.marketplace.identity.application.result;

import com.marketplace.identity.domain.model.UserAccount;
import java.util.Objects;

public record VerifyOtpResult(UserAccount account) {

    public VerifyOtpResult {
        Objects.requireNonNull(account, "account must not be null");
    }
}