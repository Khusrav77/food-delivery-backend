package com.marketplace.identity.infrastructure.event;

import com.marketplace.identity.domain.event.UserAccountCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class UserAccountCreatedEventListener {

    @EventListener
    public void handle(UserAccountCreated event) {
        System.out.println("User account created: " + event.userId());
    }
}