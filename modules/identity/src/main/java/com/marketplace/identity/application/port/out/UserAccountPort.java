package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;

import java.util.Optional;

public interface UserAccountPort {
    Optional<UserAccount> findByPhone(PhoneNumber phone);
    Optional<UserAccount> findById(UserId userId);
    UserAccount save(UserAccount userAccount);
}
