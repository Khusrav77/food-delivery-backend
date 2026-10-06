package com.marketplace.identity.application.result;

import java.util.Objects;

public record RefreshTokenResult(
        AccessToken accessToken
) {
    public RefreshTokenResult {
        Objects.requireNonNull(accessToken, "accessToken must not be null");
    }
}
