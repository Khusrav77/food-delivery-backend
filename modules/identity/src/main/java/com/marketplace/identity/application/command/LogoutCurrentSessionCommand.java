package com.marketplace.identity.application.command;

import com.marketplace.identity.domain.model.UserId;

import java.util.Objects;

public record LogoutCurrentSessionCommand(UserId userId, String refreshToken) {

    public LogoutCurrentSessionCommand {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(refreshToken, "refreshToken must not be null");
        if (refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken must not be blank");
        }
    }
}
