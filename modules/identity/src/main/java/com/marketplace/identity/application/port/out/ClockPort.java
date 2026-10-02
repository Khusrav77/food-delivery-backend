package com.marketplace.identity.application.port.out;

import java.time.Instant;

public interface ClockPort {
    Instant now();
}
