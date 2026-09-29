package com.marketplace.identity.application;

import java.time.Instant;

public interface Clock {
    Instant now();
}
