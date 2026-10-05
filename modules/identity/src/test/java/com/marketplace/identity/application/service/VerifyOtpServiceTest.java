package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.infrastructure.otp.InMemoryOtpStorageAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VerifyOtpServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-30T10:00:00Z");
    private static final Instant EXPIRES_AT = Instant.parse("2026-09-30T10:03:00Z");
    private static final PhoneNumber PHONE = new PhoneNumber("+79991234567");

    @Test
    void shouldVerifyOtpAndReturnUserId() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();

        FakeClockPort clockPort = new FakeClockPort(NOW);
        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);
        otpStorageAdapter.save(PHONE, otp);
        UserId userId = UserId.generate();
        UserAccount account = UserAccount.create(userId, PHONE);
        userAccountPort.save(account);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");
        VerifyOtpResult result = service.execute(command);

        assertEquals(userId, result.account().id());
        assertTrue(otp.isUsed());
    }

    @Test
    void shouldRejectWhenOtpDoesNotExist() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);
        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }

    @Test
    void shouldRejectInvalidOtp() {
        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);
        otpStorageAdapter.save(PHONE, otp);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "654321");

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
        assertEquals(1, otp.attempts());
    }

    @Test
    void shouldRejectExpiredOtp() {
        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();

        FakeClockPort clockPort = new FakeClockPort(Instant.parse("2026-09-30T10:03:01Z"));

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);

        otpStorageAdapter.save(PHONE, otp);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
        assertEquals(0, otp.attempts());
    }

    @Test
    void shouldReturnExistingAccount() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);

        otpStorageAdapter.save(PHONE, otp);
        UserId userId = UserId.generate();
        UserAccount existingAccount = UserAccount.create(userId, PHONE);
        userAccountPort.save(existingAccount);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");
        VerifyOtpResult result = service.execute(command);

        assertEquals(userId, result.account().id());
        assertEquals(0, createUserAccountUseCase.executionCount);
    }

    @Test
    void shouldCreateAccountWhenAccountDoesNotExist() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);
        otpStorageAdapter.save(PHONE, otp);
        UserId userId = UserId.generate();

        UserAccount createdAccount = UserAccount.create(userId, PHONE);
        createUserAccountUseCase.result = createdAccount;

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");
        VerifyOtpResult result = service.execute(command);

        assertEquals(userId, result.account().id());
        assertEquals(1, createUserAccountUseCase.executionCount);
        assertEquals(PHONE, createUserAccountUseCase.lastCommand.phone());
    }

    @Test
    void shouldRejectNullCommand() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        assertThrows(NullPointerException.class, () -> service.execute(null));
    }

    @Test
    void shouldRejectAlreadyUsedOtp() {

        InMemoryOtpStorageAdapter otpStorageAdapter = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase =
                new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);
        otpStorageAdapter.save(PHONE, otp);

        UserId userId = UserId.generate();
        UserAccount account = UserAccount.create(userId, PHONE);
        userAccountPort.save(account);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorageAdapter,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand command = new VerifyOtpCommand(PHONE, "123456");

        service.execute(command);

        assertThrows(IllegalArgumentException.class, () -> service.execute(command));
    }

    @Test
    void shouldRejectOtpAfterMaximumAttempts() {
        InMemoryOtpStorageAdapter otpStorage = new InMemoryOtpStorageAdapter();
        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        FakeCreateUserAccountUseCase createUserAccountUseCase = new FakeCreateUserAccountUseCase();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        OtpCode otp = OtpCode.create("123456", EXPIRES_AT);
        otpStorage.save(PHONE, otp);

        VerifyOtpService service =
                new VerifyOtpService(
                        otpStorage,
                        userAccountPort,
                        createUserAccountUseCase,
                        clockPort);

        VerifyOtpCommand invalidCommand = new VerifyOtpCommand(PHONE, "654321");

        for (int i = 0; i < 5; i++) {
            assertThrows(IllegalArgumentException.class,
                    () -> service.execute(invalidCommand));
        }

        assertEquals(5, otp.attempts());

        VerifyOtpCommand validCommand = new VerifyOtpCommand(PHONE, "123456");

        assertThrows(IllegalArgumentException.class, () -> service.execute(validCommand));
        assertEquals(5, otp.attempts());
    }


    private static class FakeUserAccountPort implements UserAccountPort {

        private final Map<String, UserAccount> storage = new HashMap<>();

        @Override
        public Optional<UserAccount> findByPhone(PhoneNumber phone) {
            return Optional.ofNullable(storage.get(phone.value()));
        }

        @Override
        public UserAccount save(UserAccount userAccount) {
            storage.put(userAccount.phone().value(), userAccount);
            return userAccount;
        }
    }

    private static class FakeCreateUserAccountUseCase implements CreateUserAccountUseCase {

        private UserAccount result;
        private int executionCount;
        private CreateUserAccountCommand lastCommand;

        @Override
        public UserAccount execute(CreateUserAccountCommand command) {
            executionCount++;
            lastCommand = command;
            return result;
        }
    }

    private static class FakeClockPort implements ClockPort {

        private final Instant now;
        private FakeClockPort(Instant now) {
            this.now = now;
        }

        @Override
        public Instant now() {
            return now;
        }
    }
}
