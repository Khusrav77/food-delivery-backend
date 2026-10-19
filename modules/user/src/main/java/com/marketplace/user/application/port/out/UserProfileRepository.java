package com.marketplace.user.application.port.out;

import com.marketplace.user.domain.model.AccountId;
import com.marketplace.user.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findByAccountId(AccountId accountId);
    UserProfile save(UserProfile profile);
}
