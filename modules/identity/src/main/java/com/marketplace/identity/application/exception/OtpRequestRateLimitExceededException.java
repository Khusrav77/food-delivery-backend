package com.marketplace.identity.application.exception;

public final class OtpRequestRateLimitExceededException extends RuntimeException {

    private final long retryAfterSeconds;

    public OtpRequestRateLimitExceededException(long retryAfterSeconds) {
        super("OTP request rate limit exceeded");
        if (retryAfterSeconds < 1) {
            throw new IllegalArgumentException("retryAfterSeconds must be positive");
        }
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long retryAfterSeconds() {
        return retryAfterSeconds;
    }
}
