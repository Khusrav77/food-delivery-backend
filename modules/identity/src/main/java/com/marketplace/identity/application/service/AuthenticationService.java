package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.AuthenticateCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.port.in.AuthenticateUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.AuthenticationResult;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;
import java.time.Instant;

public final class AuthenticationService implements AuthenticateUseCase {

    private final VerifyOtpUseCase verifyOtpUseCase;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshSessionPort refreshSessionPort;
    private final ClockPort clockPort;

    public AuthenticationService(
            VerifyOtpUseCase verifyOtpUseCase,
            TokenProviderPort tokenProviderPort,
            RefreshSessionPort refreshSessionPort,
            ClockPort clockPort
    ) {
        this.verifyOtpUseCase = Objects.requireNonNull(
                verifyOtpUseCase, "verifyOtpUseCase must not be null");

        this.tokenProviderPort = Objects.requireNonNull(
                tokenProviderPort, "tokenProviderPort must not be null");
        this.refreshSessionPort = Objects.requireNonNull(
                refreshSessionPort, "refreshSessionPort must not be null");
        this.clockPort = Objects.requireNonNull(clockPort, "clockPort must not be null");
    }

    @Override
    public AuthenticationResult execute(AuthenticateCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        VerifyOtpResult result = verifyOtpUseCase.execute(
                new VerifyOtpCommand(command.phone(), command.code()));

        UserAccount account = result.account();
        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);
        RefreshToken refreshToken = tokenProviderPort.generateRefreshToken(account);
        Instant now = clockPort.now();
        refreshSessionPort.start(
                account.id(),
                refreshToken.value(),
                now.plusSeconds(refreshToken.expiresInSeconds()),
                now);

        return new AuthenticationResult(accessToken, refreshToken);
    }
}
