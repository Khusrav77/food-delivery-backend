package com.marketplace.identity.infrastructure.otp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomOtpGeneratorTest {

    @Test
    void shouldGenerateSixDigitCode() {
        RandomOtpGenerator generator = new RandomOtpGenerator();

        String code = generator.generate();

        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }
}
