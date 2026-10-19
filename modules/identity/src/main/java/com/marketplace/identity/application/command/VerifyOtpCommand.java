package com.marketplace.identity.application.command;


import com.marketplace.identity.domain.model.PhoneNumber;

public record VerifyOtpCommand(
        PhoneNumber phone,
        String code
) {
}
