package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.AuthenticateCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.port.in.AuthenticateUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.port.out.TokenProviderPort;
import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.AuthenticationResult;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;

public final class AuthenticationService implements AuthenticateUseCase {

    private final VerifyOtpUseCase verifyOtpUseCase;
    private final TokenProviderPort tokenProviderPort;

    public AuthenticationService(
            VerifyOtpUseCase verifyOtpUseCase,
            TokenProviderPort tokenProviderPort
    ) {
        this.verifyOtpUseCase = Objects.requireNonNull(
                verifyOtpUseCase, "verifyOtpUseCase must not be null");

        this.tokenProviderPort = Objects.requireNonNull(
                tokenProviderPort, "tokenProviderPort must not be null");
    }

    @Override
    public AuthenticationResult execute(AuthenticateCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        VerifyOtpResult result = verifyOtpUseCase.execute(
                new VerifyOtpCommand(command.phone(), command.code()));

        UserAccount account = result.account();
        AccessToken accessToken = tokenProviderPort.generateAccessToken(account);
        RefreshToken refreshToken = tokenProviderPort.generateRefreshToken(account);

        return new AuthenticationResult(accessToken, refreshToken);
    }
}
