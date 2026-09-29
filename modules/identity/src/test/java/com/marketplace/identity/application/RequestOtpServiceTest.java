package com.marketplace.identity.application;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestOtpServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-29T10:00:00Z");

    @Test
    void shouldCreateOtpForUserAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");

        CreateUserAccountUC createUserAccountUC =
                new CreateUserAccountUC(
                        new FakeUserAccountRepository());

        OtpGenerator otpGenerator = () -> "123456";

        Clock clock = () -> NOW;

        RequestOtpService service = new RequestOtpService(
                createUserAccountUC,
                        otpGenerator,
                        clock);

        RequestOtpResult result = service.execute(phone);

        assertNotNull(result.account());
        assertEquals(phone, result.account().phone());

        assertNotNull(result.otp());
        assertEquals(0, result.otp().attempts());
        assertEquals(false, result.otp().isUsed());
    }

    @Test
    void shouldCreateOtpWithThreeMinutesExpiration() {
        PhoneNumber phone = new PhoneNumber("+79991234567");

        CreateUserAccountUC createUserAccountUC =
                new CreateUserAccountUC(
                        new FakeUserAccountRepository());

        OtpGenerator otpGenerator = () -> "123456";
        Clock clock = () -> NOW;

        RequestOtpService service = new RequestOtpService(
                createUserAccountUC,
                        otpGenerator,
                        clock);

        RequestOtpResult result = service.execute(phone);

        assertEquals(NOW.plusSeconds(180), result.otp().expiresAt());
    }

    private static class FakeUserAccountRepository
            implements com.marketplace.identity.domain.repository.UserAccountRepository {

        private UserAccount account;

        @Override
        public Optional<UserAccount> findById(UserId id) {
            if (account == null) {
                return Optional.empty();
            }

            return account.id().equals(id)
                    ? Optional.of(account)
                    : Optional.empty();
        }

        @Override
        public Optional<UserAccount> findByPhone(PhoneNumber phone) {
            if (account == null) {
                return Optional.empty();
            }

            return account.phone().equals(phone)
                    ? Optional.of(account)
                    : Optional.empty();
        }

        @Override
        public UserAccount save(UserAccount account) {
            this.account = account;
            return account;
        }
    }
}