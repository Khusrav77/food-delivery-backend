package com.marketplace.identity.application.port.out;

import com.marketplace.identity.application.result.AccessToken;
import com.marketplace.identity.application.result.RefreshToken;
import com.marketplace.identity.domain.model.UserAccount;

public interface TokenProviderPort {
    AccessToken generateAccessToken(UserAccount account);
    RefreshToken generateRefreshToken(UserAccount account);
}
