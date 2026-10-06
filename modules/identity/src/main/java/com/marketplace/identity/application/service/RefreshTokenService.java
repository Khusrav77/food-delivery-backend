package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.port.in.RefreshTokenUseCase;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshTokenResult;
import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;

public final class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenValidatorPort tokenValidatorPort;
    private final UserAccountPort userAccountPort;
    private final TokenProviderPort tokenProviderPort;

    public RefreshTokenService(
            TokenValidatorPort tokenValidatorPort,
            UserAccountPort userAccountPort,
            TokenProviderPort tokenProviderPort) {
        this.tokenValidatorPort = Objects.requireNonNull(
                tokenValidatorPort, "tokenValidatorPort must not be null");
        this.userAccountPort = Objects.requireNonNull(
                userAccountPort, "userAccountPort must not be null");
        this.tokenProviderPort = Objects.requireNonNull(
                tokenProviderPort, "tokenProviderPort must not be null");
    }

    @Override
    public RefreshTokenResult execute(RefreshTokenCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        ValidatedToken validatedToken =
                tokenValidatorPort.validateRefreshToken(command.refreshToken());

        UserAccount account = userAccountPort
                .findById(validatedToken.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User account not found"));

        if (!account.isActive()) {
            throw new IllegalStateException("User account is not active");
        }

        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);
        return new RefreshTokenResult(accessToken);
    }
}
