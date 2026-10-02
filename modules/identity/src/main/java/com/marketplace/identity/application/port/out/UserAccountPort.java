package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;

import java.util.Optional;

public interface UserAccountPort {
    Optional<UserAccount> findByPhone(PhoneNumber phone);
    UserAccount save(UserAccount userAccount);
}
