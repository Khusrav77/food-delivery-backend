package com.marketplace.identity.application.service;


import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.port.in.RequestOtpUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.application.port.out.OtpGeneratorPort;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.domain.model.OtpCode;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class RequestOtpService implements RequestOtpUseCase {

    private static final Duration OTP_TTL = Duration.ofMinutes(3);
    private final OtpStoragePort otpStoragePort;
    private final OtpGeneratorPort otpGeneratorPort;
    private final ClockPort clockPort;

    public RequestOtpService(
            OtpStoragePort otpStoragePort,
            OtpGeneratorPort otpGeneratorPort,
            ClockPort clockPort
    ) {
        this.otpStoragePort = Objects.requireNonNull(
                otpStoragePort, "otpCodePort must not be null");

        this.otpGeneratorPort = Objects.requireNonNull(
                otpGeneratorPort, "otpGeneratorPort must not be null");

        this.clockPort = Objects.requireNonNull(
                clockPort, "clockPort must not be null");
    }

    @Override
    public RequestOtpResult execute(RequestOtpCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        Instant now = clockPort.now();
        Instant expiresAt = now.plus(OTP_TTL);
        String value = otpGeneratorPort.generate();
        OtpCode otpCode = OtpCode.create(value, expiresAt);
        otpStoragePort.save(command.phone(), otpCode);
        return new RequestOtpResult(OTP_TTL.toSeconds());
    }
}