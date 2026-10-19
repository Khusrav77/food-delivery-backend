package com.marketplace.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}