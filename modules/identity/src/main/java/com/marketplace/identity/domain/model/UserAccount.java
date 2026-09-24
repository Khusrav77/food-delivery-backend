package com.marketplace.identity.domain.model;

import java.util.Objects;

public final class UserAccount {

    private final UserId id;
    private AccountStatus status;

    private UserAccount(UserId id, AccountStatus status) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public static UserAccount create(UserId id) {
        return new UserAccount(id, AccountStatus.ACTIVE);
    }

    public UserId id() {
        return id;
    }

    public AccountStatus status() {
        return status;
    }

    public void block() {
        if (status == AccountStatus.DISABLED) {
            throw new IllegalStateException("Disabled account cannot be blocked");
        }

        status = AccountStatus.BLOCKED;
    }

    public void disable() {
        status = AccountStatus.DISABLED;
    }

    public void activate() {
        if (status == AccountStatus.DISABLED) {
            throw new IllegalStateException("Disabled account cannot be activated");
        }

        status = AccountStatus.ACTIVE;
    }
}
