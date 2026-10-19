package com.marketplace.identity.application.result;

import java.util.Objects;

public record AuthenticationResult(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
    public AuthenticationResult {
        Objects.requireNonNull(accessToken, "accessToken must not be null");
        Objects.requireNonNull(refreshToken, "refreshToken must not be null");
    }
}