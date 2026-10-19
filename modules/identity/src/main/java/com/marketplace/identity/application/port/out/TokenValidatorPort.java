package com.marketplace.identity.application.port.out;

import com.marketplace.identity.application.result.ValidatedToken;

public interface TokenValidatorPort {
    ValidatedToken validateAccessToken(String token);
    ValidatedToken validateRefreshToken(String token);
}
