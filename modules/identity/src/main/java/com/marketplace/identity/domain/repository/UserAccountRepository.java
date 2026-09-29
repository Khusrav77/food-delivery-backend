package com.marketplace.identity.domain.repository;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;

import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> findById(UserId id);
    Optional<UserAccount> findByPhone(PhoneNumber phone);
    UserAccount save(UserAccount userAccount);

}
