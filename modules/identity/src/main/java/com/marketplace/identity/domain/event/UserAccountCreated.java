package com.marketplace.identity.domain.event;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.event.DomainEvent;

import java.time.Instant;
import java.util.Objects;

public record UserAccountCreated(
        UserId userId,
        PhoneNumber phone,
        Instant occurredAt
) implements DomainEvent {

    public UserAccountCreated {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
