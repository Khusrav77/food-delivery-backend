package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.exception.AuthenticationFailedException;
import com.marketplace.identity.application.port.in.RefreshTokenUseCase;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.TokenValidatorPort;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.application.result.RefreshTokenResult;
import com.marketplace.identity.application.result.ValidatedToken;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;
import java.time.Instant;

public final class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenValidatorPort tokenValidatorPort;
    private final UserAccountPort userAccountPort;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshSessionPort refreshSessionPort;
    private final ClockPort clockPort;

    public RefreshTokenService(
            TokenValidatorPort tokenValidatorPort,
            UserAccountPort userAccountPort,
            TokenProviderPort tokenProviderPort,
            RefreshSessionPort refreshSessionPort,
            ClockPort clockPort) {
        this.tokenValidatorPort = Objects.requireNonNull(
                tokenValidatorPort, "tokenValidatorPort must not be null");
        this.userAccountPort = Objects.requireNonNull(
                userAccountPort, "userAccountPort must not be null");
        this.tokenProviderPort = Objects.requireNonNull(
                tokenProviderPort, "tokenProviderPort must not be null");
        this.refreshSessionPort = Objects.requireNonNull(
                refreshSessionPort, "refreshSessionPort must not be null");
        this.clockPort = Objects.requireNonNull(clockPort, "clockPort must not be null");
    }

    @Override
    public RefreshTokenResult execute(RefreshTokenCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        ValidatedToken validatedToken;
        try {
            validatedToken = tokenValidatorPort.validateRefreshToken(command.refreshToken());
        } catch (IllegalArgumentException exception) {
            throw new AuthenticationFailedException();
        }

        UserAccount account = userAccountPort
                .findById(validatedToken.userId())
                .orElseThrow(() ->
                        new AuthenticationFailedException());

        if (!account.isActive()) {
            throw new AuthenticationFailedException();
        }

        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);
        RefreshToken refreshToken = tokenProviderPort.generateRefreshToken(account);
        Instant now = clockPort.now();
        RefreshSessionPort.RotationResult rotation = refreshSessionPort.rotate(
                validatedToken.userId(),
                command.refreshToken(),
                refreshToken.value(),
                now.plusSeconds(refreshToken.expiresInSeconds()),
                now);

        if (rotation != RefreshSessionPort.RotationResult.ROTATED) {
            throw new AuthenticationFailedException();
        }

        return new RefreshTokenResult(accessToken, refreshToken);
    }
}
