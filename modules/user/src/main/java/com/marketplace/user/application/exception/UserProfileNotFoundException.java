package com.marketplace.user.application.exception;

import java.util.UUID;

public final class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(UUID accountId) {
        super("User profile not found for account " + accountId);
    }
}
