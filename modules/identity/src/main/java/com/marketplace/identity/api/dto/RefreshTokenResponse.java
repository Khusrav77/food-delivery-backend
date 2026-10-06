package com.marketplace.identity.api.dto;

public record RefreshTokenResponse(
        String accessToken,
        long accessTokenExpiresIn
) {
}
