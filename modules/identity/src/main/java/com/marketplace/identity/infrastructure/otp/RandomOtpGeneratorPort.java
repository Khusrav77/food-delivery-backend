package com.marketplace.identity.infrastructure.otp;

import com.marketplace.identity.application.port.out.OtpGeneratorPort;

import java.security.SecureRandom;


public class RandomOtpGeneratorPort implements OtpGeneratorPort {
    private static final int OTP_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        int value = random.nextInt(1_000_000);
        return String.format("%0" + OTP_LENGTH + "d", value);
    }
}
