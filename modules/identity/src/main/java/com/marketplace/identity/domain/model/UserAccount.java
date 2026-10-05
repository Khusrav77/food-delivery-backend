package com.marketplace.identity.domain.model;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class UserAccount {

    private final UserId id;
    private final PhoneNumber phone;
    private final Set<Role> roles;
    private AccountStatus status;

    private UserAccount(UserId id, PhoneNumber phone, Set<Role> roles, AccountStatus status) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.phone = Objects.requireNonNull(phone, "phone must not be null");
        this.roles = EnumSet.copyOf(
                Objects.requireNonNull(roles, "roles must not be null"));
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public static UserAccount create(UserId id, PhoneNumber phone) {
        return new UserAccount(id, phone, EnumSet.of(Role.USER), AccountStatus.ACTIVE);
    }

    public UserId id() {
        return id;
    }
    public PhoneNumber phone() {
        return phone;
    }
    public AccountStatus status() {
        return status;
    }
    public Set<Role> roles() {
        return Set.copyOf(roles);
    }
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
    public boolean isBlocked() {
        return status == AccountStatus.BLOCKED;
    }
    public boolean isDisabled() {
        return status == AccountStatus.DISABLED;
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
