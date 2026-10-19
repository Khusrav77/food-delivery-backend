package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.PhoneNumber;

public interface OtpRequestRateLimiterPort {

    OtpRequestRateLimitDecision tryAcquire(PhoneNumber phone, String clientIp);
}
