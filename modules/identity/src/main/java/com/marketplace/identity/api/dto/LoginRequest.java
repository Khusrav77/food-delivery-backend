package com.marketplace.identity.api.dto;

public record LoginRequest(
        String phone,
        String code
) {
}
