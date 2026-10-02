package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpGeneratorPort;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.infrastructure.otp.InMemoryOtpStorageAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestOtpServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-30T10:00:00Z");
    private static final String OTP_VALUE = "123456";
    private static final PhoneNumber PHONE = new PhoneNumber("+49123456789");

    @Test
    void shouldGenerateAndSaveOtp() {

        InMemoryOtpStorageAdapter otpStorage = new InMemoryOtpStorageAdapter();
        RequestOtpService service = new RequestOtpService(
                        otpStorage,
                        new FixedOtpGenerator(OTP_VALUE),
                        new FixedClock(NOW));

        RequestOtpResult result = service.execute(new RequestOtpCommand(PHONE));

        OtpCode otpCode = otpStorage.findByPhone(PHONE).orElseThrow();

        assertEquals(180, result.expiresInSeconds());
        assertNotNull(otpCode);
        assertFalse(otpCode.isUsed());
        assertEquals(0, otpCode.attempts());
        assertEquals(NOW.plusSeconds(180), otpCode.expiresAt());
    }

    @Test
    void shouldUseOtpGenerator() {

        InMemoryOtpStorageAdapter otpStorage = new InMemoryOtpStorageAdapter();
        RequestOtpService service = new RequestOtpService(
                        otpStorage,
                        new FixedOtpGenerator(OTP_VALUE),
                        new FixedClock(NOW));

        service.execute(new RequestOtpCommand(PHONE));
        OtpCode otpCode = otpStorage.findByPhone(PHONE).orElseThrow();

        assertTrue(otpCode.verify(OTP_VALUE, NOW.plusSeconds(179)));
    }

    @Test
    void shouldUseCurrentTimeFromClock() {

        Instant customNow = Instant.parse("2026-09-30T15:30:00Z");
        InMemoryOtpStorageAdapter otpStorage = new InMemoryOtpStorageAdapter();
        RequestOtpService service = new RequestOtpService(
                        otpStorage,
                        new FixedOtpGenerator(OTP_VALUE),
                        new FixedClock(customNow));

        service.execute(new RequestOtpCommand(PHONE));
        OtpCode otpCode = otpStorage.findByPhone(PHONE).orElseThrow();

        assertEquals(customNow.plusSeconds(180), otpCode.expiresAt());
    }

    @Test
    void shouldRejectNullCommand() {

        RequestOtpService service = new RequestOtpService(
                        new InMemoryOtpStorageAdapter(),
                        new FixedOtpGenerator(OTP_VALUE),
                        new FixedClock(NOW));

        assertThrows(NullPointerException.class, () -> service.execute(null));
    }

    @Test
    void shouldCreateOtpThatCanBeVerifiedBeforeExpiration() {

        InMemoryOtpStorageAdapter otpStorage = new InMemoryOtpStorageAdapter();
        RequestOtpService service = new RequestOtpService(
                        otpStorage,
                        new FixedOtpGenerator(OTP_VALUE),
                        new FixedClock(NOW));

        service.execute(new RequestOtpCommand(PHONE));
        OtpCode otpCode = otpStorage.findByPhone(PHONE).orElseThrow();

        assertTrue(otpCode.verify(OTP_VALUE, NOW.plusSeconds(179)));
    }

    private static final class FixedClock implements ClockPort {

        private final Instant now;

        private FixedClock(Instant now) {
            this.now = now;
        }

        @Override
        public Instant now() {
            return now;
        }
    }

    private static final class FixedOtpGenerator implements OtpGeneratorPort {

        private final String value;

        private FixedOtpGenerator(String value) {
            this.value = value;
        }

        @Override
        public String generate() {
            return value;
        }
    }
}