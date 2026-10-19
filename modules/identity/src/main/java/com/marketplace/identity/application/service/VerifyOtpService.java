package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.exception.AuthenticationFailedException;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;

import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Objects;

public final class VerifyOtpService implements VerifyOtpUseCase {

    private final OtpStoragePort otpStoragePort;
    private final UserAccountPort userAccountPort;
    private final CreateUserAccountUseCase createUserAccountUseCase;
    private final ClockPort clockPort;

    public VerifyOtpService(
            OtpStoragePort otpStoragePort,
            UserAccountPort userAccountPort,
            CreateUserAccountUseCase createUserAccountUseCase,
            ClockPort clockPort
    ) {
        this.otpStoragePort = Objects.requireNonNull(
                otpStoragePort, "otpCodePort must not be null");

        this.userAccountPort = Objects.requireNonNull(
                userAccountPort, "userAccountRepository must not be null");

        this.createUserAccountUseCase = Objects.requireNonNull(
                createUserAccountUseCase, "createUserAccountUseCase must not be null");

        this.clockPort = Objects.requireNonNull(
                clockPort, "clockPort must not be null");
    }

    @Override
    public VerifyOtpResult execute(VerifyOtpCommand command) {

        Objects.requireNonNull(command, "command must not be null");
        if (!otpStoragePort.verify(command.phone(), command.code(), clockPort.now())) {
            throw new AuthenticationFailedException();
        }

        UserAccount account = userAccountPort
                .findByPhone(command.phone())
                .orElseGet(() -> createUserAccountUseCase.execute(
                                new CreateUserAccountCommand(command.phone())));
        return new VerifyOtpResult(account);
    }
}
