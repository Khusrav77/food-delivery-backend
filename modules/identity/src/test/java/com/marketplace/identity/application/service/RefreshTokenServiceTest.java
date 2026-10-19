package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.application.result.RefreshTokenResult;
import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshTokenServiceTest {

    private static final UserId USER_ID = new UserId(UUID.randomUUID());
    private final UserAccount account = UserAccount.create(
            USER_ID, new PhoneNumber("+12025550123"));

    private StubTokenValidator tokenValidator;
    private StubTokenProvider tokenProvider;
    private StubUserAccountPort userAccountPort;
    private StubRefreshSessionPort refreshSessionPort;
    private RefreshTokenService service;

    @BeforeEach
    void setUp() {
        tokenValidator = new StubTokenValidator(new ValidatedToken(USER_ID, account.roles()));
        tokenProvider = new StubTokenProvider();
        userAccountPort = new StubUserAccountPort(account);
        refreshSessionPort = new StubRefreshSessionPort();
        ClockPort clock = () -> Instant.parse("2026-10-19T12:00:00Z");
        service = new RefreshTokenService(
                tokenValidator, userAccountPort, tokenProvider, refreshSessionPort, clock);
    }

    @Test
    void shouldRotateRefreshTokenAndReturnNewTokenPair() {
        RefreshTokenResult result = service.execute(new RefreshTokenCommand("refresh-token"));

        assertEquals("new-access-token", result.accessToken().value());
        assertEquals("new-refresh-token", result.refreshToken().value());
        assertEquals("refresh-token", tokenValidator.receivedToken);
        assertEquals("refresh-token", refreshSessionPort.presentedToken);
        assertEquals("new-refresh-token", refreshSessionPort.replacementToken);
        assertEquals(RefreshSessionPort.RotationResult.ROTATED, refreshSessionPort.result);
        assertEquals(Instant.parse("2026-10-19T12:30:00Z"), refreshSessionPort.expiresAt);
    }

    @Test
    void shouldRejectReusedRefreshToken() {
        refreshSessionPort.result = RefreshSessionPort.RotationResult.REUSED;

        assertThrows(IllegalArgumentException.class,
                () -> service.execute(new RefreshTokenCommand("old-refresh-token")));
    }

    @Test
    void shouldRejectRefreshTokenForMissingAccount() {
        userAccountPort.account = null;

        assertThrows(IllegalArgumentException.class,
                () -> service.execute(new RefreshTokenCommand("refresh-token")));
    }

    @Test
    void shouldRejectRefreshTokenForInactiveAccount() {
        account.disable();

        assertThrows(IllegalStateException.class,
                () -> service.execute(new RefreshTokenCommand("refresh-token")));
    }

    @Test
    void shouldRejectNullCommand() {
        assertThrows(NullPointerException.class, () -> service.execute(null));
    }

    private static final class StubTokenValidator implements TokenValidatorPort {
        private final ValidatedToken validatedToken;
        private String receivedToken;

        private StubTokenValidator(ValidatedToken validatedToken) {
            this.validatedToken = validatedToken;
        }

        @Override
        public ValidatedToken validateAccessToken(String token) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValidatedToken validateRefreshToken(String token) {
            receivedToken = token;
            return validatedToken;
        }
    }

    private static final class StubTokenProvider implements TokenProviderPort {
        @Override
        public AccessToken generateAccessToken(UserAccount userAccount) {
            return new AccessToken("new-access-token", 900);
        }

        @Override
        public RefreshToken generateRefreshToken(UserAccount userAccount) {
            return new RefreshToken("new-refresh-token", 1800);
        }
    }

    private static final class StubRefreshSessionPort implements RefreshSessionPort {
        private RotationResult result = RotationResult.ROTATED;
        private String presentedToken;
        private String replacementToken;
        private Instant expiresAt;

        @Override
        public void start(UserId userId, String refreshToken, Instant expiresAt, Instant now) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RotationResult rotate(
                UserId userId,
                String presentedToken,
                String replacementToken,
                Instant replacementExpiresAt,
                Instant now) {
            this.presentedToken = presentedToken;
            this.replacementToken = replacementToken;
            this.expiresAt = replacementExpiresAt;
            return result;
        }

        @Override
        public void revokeCurrent(UserId userId, String refreshToken, Instant now) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void revokeAll(UserId userId, Instant now) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class StubUserAccountPort implements UserAccountPort {
        private UserAccount account;

        private StubUserAccountPort(UserAccount account) {
            this.account = account;
        }

        @Override
        public Optional<UserAccount> findByPhone(PhoneNumber phone) {
            return Optional.empty();
        }

        @Override
        public Optional<UserAccount> findById(UserId userId) {
            return Optional.ofNullable(account);
        }

        @Override
        public UserAccount save(UserAccount userAccount) {
            account = userAccount;
            return userAccount;
        }
    }
}
