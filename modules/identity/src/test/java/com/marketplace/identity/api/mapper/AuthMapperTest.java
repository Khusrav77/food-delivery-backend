package com.marketplace.identity.api.mapper;

import com.marketplace.identity.application.result.RequestOtpResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthMapperTest {

    @Test
    void shouldOmitOtpCodeUnlessExplicitlyEnabled() {
        var response = AuthMapper.toResponse(new RequestOtpResult(180, "123456"), false);

        assertEquals(180, response.expiresInSeconds());
        assertNull(response.debugCode());
    }

    @Test
    void shouldIncludeOtpCodeWhenExplicitlyEnabled() {
        var response = AuthMapper.toResponse(new RequestOtpResult(180, "123456"), true);

        assertEquals("123456", response.debugCode());
    }
}
