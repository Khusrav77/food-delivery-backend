package com.marketplace.identity.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OtpCodeTest {

    @Test
    void shouldCreateValidOtpCode() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        assertFalse(otp.isUsed());
        assertEquals(0, otp.attempts());
    }

    @Test
    void shouldRejectInvalidOtpCode() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");

        assertThrows(IllegalArgumentException.class,
                () -> OtpCode.create("12345", expiresAt));
    }

    @Test
    void shouldVerifyCorrectCode() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");
        Instant now = Instant.parse("2026-09-24T12:01:00Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        boolean result = otp.verify("123456", now);

        assertTrue(result);
        assertTrue(otp.isUsed());
        assertEquals(1, otp.attempts());
    }

    @Test
    void shouldRejectIncorrectCode() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");
        Instant now = Instant.parse("2026-09-24T12:01:00Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        boolean result = otp.verify("654321", now);

        assertFalse(result);
        assertFalse(otp.isUsed());
        assertEquals(1, otp.attempts());
    }

    @Test
    void shouldNotVerifyExpiredCode() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");
        Instant now = Instant.parse("2026-09-24T12:03:01Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        boolean result = otp.verify("123456", now);

        assertFalse(result);
        assertFalse(otp.isUsed());
        assertEquals(0, otp.attempts());
    }

    @Test
    void shouldNotVerifyCodeTwice() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");
        Instant now = Instant.parse("2026-09-24T12:01:00Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        assertTrue(otp.verify("123456", now));
        assertFalse(otp.verify("123456", now));

        assertEquals(1, otp.attempts());
        assertTrue(otp.isUsed());
    }

    @Test
    void shouldAllowOnlyFiveAttempts() {
        Instant expiresAt = Instant.parse("2026-09-24T12:03:00Z");
        Instant now = Instant.parse("2026-09-24T12:01:00Z");

        OtpCode otp = OtpCode.create("123456", expiresAt);

        assertFalse(otp.verify("000000", now));
        assertFalse(otp.verify("000000", now));
        assertFalse(otp.verify("000000", now));
        assertFalse(otp.verify("000000", now));
        assertFalse(otp.verify("000000", now));

        assertEquals(5, otp.attempts());

        assertFalse(otp.verify("123456", now));
        assertEquals(5, otp.attempts());
        assertFalse(otp.isUsed());
    }

    @Test
    void shouldRejectNullExpirationTime() {
        assertThrows(NullPointerException.class,
                () -> OtpCode.create("123456", null));
    }
}

