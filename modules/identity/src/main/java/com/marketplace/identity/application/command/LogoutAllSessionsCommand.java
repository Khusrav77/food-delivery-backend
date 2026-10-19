package com.marketplace.identity.application.command;

import com.marketplace.identity.domain.model.UserId;

import java.util.Objects;

public record LogoutAllSessionsCommand(UserId userId) {

    public LogoutAllSessionsCommand {
        Objects.requireNonNull(userId, "userId must not be null");
    }
}
