package com.marketplace.identity.application.command;

import com.marketplace.identity.domain.model.PhoneNumber;

public record AuthenticateCommand(
        PhoneNumber phone,
        String code
) {
}
