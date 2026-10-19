package com.marketplace.identity.application.port.out;

public record OtpRequestRateLimitDecision(boolean allowed, long retryAfterSeconds) {

    public OtpRequestRateLimitDecision {
        if (retryAfterSeconds < 0) {
            throw new IllegalArgumentException("retryAfterSeconds must not be negative");
        }
        if (allowed && retryAfterSeconds != 0) {
            throw new IllegalArgumentException("allowed request cannot have a retry delay");
        }
        if (!allowed && retryAfterSeconds == 0) {
            throw new IllegalArgumentException("denied request must have a retry delay");
        }
    }

    public static OtpRequestRateLimitDecision permitted() {
        return new OtpRequestRateLimitDecision(true, 0);
    }

    public static OtpRequestRateLimitDecision denied(long retryAfterSeconds) {
        return new OtpRequestRateLimitDecision(false, Math.max(1, retryAfterSeconds));
    }
}
