package com.marketplace.identity.api.dto;

public record VerifyOtpRequest(
        String phone,
        String code
) {
}
