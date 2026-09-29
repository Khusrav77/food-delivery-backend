package com.marketplace.identity.application;

import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class RequestOtpService {

    private static final Duration OTP_TTL = Duration.ofMinutes(3);
    private final CreateUserAccountUC createUserAccountUC;
    private final OtpGenerator otpGenerator;
    private final Clock clock;

    public RequestOtpService(
            CreateUserAccountUC createUserAccountUC,
            OtpGenerator otpGenerator,
            Clock clock
    ) {
        this.createUserAccountUC = Objects.requireNonNull(
                createUserAccountUC,
                "createUserAccountService must not be null");

        this.otpGenerator = Objects.requireNonNull(
                otpGenerator,
                "otpGenerator must not be null");

        this.clock = Objects.requireNonNull(
                clock,
                "clock must not be null");
    }

    public RequestOtpResult execute(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");

        UserAccount account = createUserAccountUC.execute(phone);

        Instant now = clock.now();
        Instant expiresAt = now.plus(OTP_TTL);

        String value = otpGenerator.generate();

        OtpCode otpCode = OtpCode.create(value, expiresAt);

        return new RequestOtpResult(account, otpCode);
    }
}
