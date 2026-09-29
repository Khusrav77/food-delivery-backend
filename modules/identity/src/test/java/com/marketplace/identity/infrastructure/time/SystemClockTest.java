package com.marketplace.identity.infrastructure.time;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemClockTest {

    @Test
    void shouldReturnCurrentTime() {
        SystemClock clock = new SystemClock();

        Instant before = Instant.now();

        Instant now = clock.now();

        Instant after = Instant.now();

        assertNotNull(now);
        assertTrue(!now.isBefore(before));
        assertTrue(!now.isAfter(after));
    }
}