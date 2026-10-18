package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshTokenServiceTest {

    private static final UserId USER_ID = new UserId(UUID.randomUUID());
    private final UserAccount account = UserAccount.create(
            USER_ID, new PhoneNumber("+12025550123"));

    private StubTokenValidator tokenValidator;
    private StubTokenProvider tokenProvider;
    private StubUserAccountPort userAccountPort;
    private RefreshTokenService service;

    @BeforeEach
    void setUp() {
        tokenValidator = new StubTokenValidator(new ValidatedToken(USER_ID, account.roles()));
        tokenProvider = new StubTokenProvider();
        userAccountPort = new StubUserAccountPort(account);
        service = new RefreshTokenService(tokenValidator, userAccountPort, tokenProvider);
    }

    @Test
    void shouldIssueNewAccessTokenForValidRefreshToken() {
        var result = service.execute(new RefreshTokenCommand("refresh-token"));

        assertEquals("new-access-token", result.accessToken().value());
        assertEquals("refresh-token", tokenValidator.receivedToken);
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
