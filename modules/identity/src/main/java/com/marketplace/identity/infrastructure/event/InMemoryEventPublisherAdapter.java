package com.marketplace.identity.infrastructure.event;

import com.marketplace.identity.application.port.out.EventPublisherPort;
import com.marketplace.event.DomainEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InMemoryEventPublisherAdapter implements EventPublisherPort {

    private final List<DomainEvent> events = new ArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        events.add(event);
    }

    public List<DomainEvent> events() {
        return List.copyOf(events);
    }

    public void clear() {
        events.clear();
    }
}
