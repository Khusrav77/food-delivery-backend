package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpCodePort;

import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.UserAccount;

import java.time.Instant;
import java.util.Objects;

public final class VerifyOtpService implements VerifyOtpUseCase {

    private final OtpCodePort otpCodePort;
    private final UserAccountPort userAccountPort;
    private final CreateUserAccountUseCase createUserAccountUseCase;
    private final ClockPort clockPort;

    public VerifyOtpService(
            OtpCodePort otpCodePort,
            UserAccountPort userAccountPort,
            CreateUserAccountUseCase createUserAccountUseCase,
            ClockPort clockPort
    ) {
        this.otpCodePort = Objects.requireNonNull(
                otpCodePort, "otpCodePort must not be null");

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

        OtpCode otpCode = otpCodePort.findByPhone(command.phone())
                .orElseThrow(() -> new IllegalArgumentException("OTP code not found"));

        Instant now = clockPort.now();
        boolean verified = otpCode.verify(command.code(), now);

        if (!verified) {
            throw new IllegalArgumentException("Invalid OTP code");
        }

        UserAccount account = userAccountPort
                .findByPhone(command.phone())
                .orElseGet(() ->
                        createUserAccountUseCase.execute(
                                new CreateUserAccountCommand(command.phone())));

        return new VerifyOtpResult(account.id());
    }
}