package com.marketplace.identity.infrastructure.time;

import com.marketplace.identity.application.Clock;

import java.time.Instant;

public class SystemClock implements Clock {


    @Override
    public Instant now() {
        return Instant.now();
    }
}
