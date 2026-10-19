package com.marketplace.identity.application.command;


import com.marketplace.identity.domain.model.PhoneNumber;

import java.util.Objects;

public record RequestOtpCommand(
        PhoneNumber phone,
        String clientIp
) {
    public RequestOtpCommand {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(clientIp, "clientIp must not be null");
        if (clientIp.isBlank()) {
            throw new IllegalArgumentException("clientIp must not be blank");
        }
    }
}
