package com.marketplace.identity.application.port.out;

import com.marketplace.event.DomainEvent;

public interface EventPublisherPort {
    void publish(DomainEvent event);
}
