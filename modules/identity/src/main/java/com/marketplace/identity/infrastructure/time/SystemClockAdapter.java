package com.marketplace.identity.infrastructure.time;

import com.marketplace.identity.application.port.out.ClockPort;

import java.time.Instant;

public class SystemClockAdapter implements ClockPort {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
