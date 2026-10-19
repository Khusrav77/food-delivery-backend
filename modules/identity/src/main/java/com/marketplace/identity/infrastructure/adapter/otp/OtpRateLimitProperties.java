package com.marketplace.identity.infrastructure.adapter.otp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "identity.otp.rate-limit")
public record OtpRateLimitProperties(
        int perPhoneRequestsPerMinute,
        int perPhoneRequestsPer15Minutes,
        int perIpRequestsPer15Minutes,
        int perIpRequestsPerDay,
        List<String> trustedProxies
) {
    public OtpRateLimitProperties {
        if (perPhoneRequestsPerMinute < 1
                || perPhoneRequestsPer15Minutes < 1
                || perIpRequestsPer15Minutes < 1
                || perIpRequestsPerDay < 1) {
            throw new IllegalArgumentException("OTP rate limits must be positive");
        }
        trustedProxies = trustedProxies == null ? List.of() : List.copyOf(trustedProxies);
    }
}
