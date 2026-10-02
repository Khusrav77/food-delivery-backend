package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpCodePort;
import com.marketplace.identity.application.port.out.OtpGeneratorPort;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestOtpServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-30T10:00:00Z");
    private static final String OTP_VALUE = "123456";

    @Test
    void shouldGenerateAndSaveOtp() {

        FakeClock clock = new FakeClock(NOW);
        FakeOtpGenerator otpGenerator = new FakeOtpGenerator(OTP_VALUE);
        FakeOtpCodePort otpCodePort = new FakeOtpCodePort();
        RequestOtpService service = new RequestOtpService(
                otpCodePort,
                otpGenerator,
                clock);

        PhoneNumber phone = new PhoneNumber("+49123456789");
        RequestOtpCommand command = new RequestOtpCommand(phone);
        RequestOtpResult result = service.execute(command);

        assertEquals(180, result.expiresInSeconds());
        assertEquals(phone, otpCodePort.savedPhone);
        assertNotNull(otpCodePort.savedOtp);

        assertFalse(otpCodePort.savedOtp.isUsed());
        assertEquals(0, otpCodePort.savedOtp.attempts());
    }

    @Test
    void shouldCreateOtpWithThreeMinutesExpiration() {

        FakeClock clock = new FakeClock(NOW);
        FakeOtpGenerator otpGenerator = new FakeOtpGenerator(OTP_VALUE);
        FakeOtpCodePort otpCodePort = new FakeOtpCodePort();
        RequestOtpService service = new RequestOtpService(
                otpCodePort,
                otpGenerator,
                clock);

        PhoneNumber phone = new PhoneNumber("+49123456789");
        service.execute(new RequestOtpCommand(phone));
        OtpCode otpCode = otpCodePort.savedOtp;

        assertNotNull(otpCode);

        assertEquals(NOW.plusSeconds(180), otpCode.expiresAt());
    }

    @Test
    void shouldUseOtpGenerator() {

        FakeOtpGenerator otpGenerator = new FakeOtpGenerator(OTP_VALUE);
        FakeOtpCodePort otpCodePort = new FakeOtpCodePort();
        RequestOtpService service = new RequestOtpService(
                otpCodePort,
                otpGenerator,
                new FakeClock(NOW));

        PhoneNumber phone = new PhoneNumber("+49123456789");
        service.execute(new RequestOtpCommand(phone));

        assertTrue(otpGenerator.generated);
    }

    @Test
    void shouldUseCurrentTimeFromClock() {

        Instant customNow = Instant.parse("2026-09-30T15:30:00Z");
        FakeClock clock = new FakeClock(customNow);
        FakeOtpCodePort otpCodePort = new FakeOtpCodePort();
        RequestOtpService service = new RequestOtpService(
                otpCodePort,
                new FakeOtpGenerator(OTP_VALUE),
                clock);

        PhoneNumber phone = new PhoneNumber("+49123456789");
        service.execute(new RequestOtpCommand(phone));

        assertEquals(customNow, clock.requestedTime);
        assertEquals(
                customNow.plusSeconds(180),
                otpCodePort.savedOtp.expiresAt());
    }

    @Test
    void shouldRejectNullCommand() {

        RequestOtpService service = new RequestOtpService(
                new FakeOtpCodePort(),
                new FakeOtpGenerator(OTP_VALUE),
                new FakeClock(NOW));

        assertThrows(NullPointerException.class, () -> service.execute(null));
    }

    @Test
    void shouldCreateOtpThatCanBeVerifiedBeforeExpiration() {

        FakeOtpCodePort otpCodePort = new FakeOtpCodePort();

        RequestOtpService service = new RequestOtpService(
                otpCodePort,
                new FakeOtpGenerator(OTP_VALUE),
                new FakeClock(NOW));

        PhoneNumber phone = new PhoneNumber("+49123456789");
        service.execute(new RequestOtpCommand(phone));

        assertTrue(otpCodePort.savedOtp.verify(OTP_VALUE, NOW.plusSeconds(179)));
    }

    private static final class FakeClock implements ClockPort {

        private final Instant now;
        private Instant requestedTime;

        private FakeClock(Instant now) {
            this.now = now;
        }

        @Override
        public Instant now() {
            requestedTime = now;
            return now;
        }
    }

    private static final class FakeOtpGenerator implements OtpGeneratorPort {

        private final String value;
        private boolean generated;

        private FakeOtpGenerator(String value) {
            this.value = value;
        }

        @Override
        public String generate() {
            generated = true;
            return value;
        }
    }

    private static final class FakeOtpCodePort implements OtpCodePort {

        private PhoneNumber savedPhone;
        private OtpCode savedOtp;

        @Override
        public void save(PhoneNumber phone, OtpCode otp) {
            this.savedPhone = phone;
            this.savedOtp = otp;
        }

        @Override
        public Optional<OtpCode> findByPhone(PhoneNumber phone) {
            return Optional.ofNullable(savedOtp);
        }

        @Override
        public void deleteByPhone(PhoneNumber phone) {
            savedPhone = null;
            savedOtp = null;
        }
    }
}
