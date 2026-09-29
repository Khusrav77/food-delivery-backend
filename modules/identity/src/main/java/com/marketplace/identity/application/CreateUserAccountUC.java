package com.marketplace.identity.application;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.domain.repository.UserAccountRepository;

import java.util.Objects;

public final class CreateUserAccountUC {

    private final UserAccountRepository userAccountRepository;

    public CreateUserAccountUC(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = Objects.requireNonNull(
                userAccountRepository,
                "userAccountRepository must not be null");
    }

    public UserAccount execute(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");

        return userAccountRepository
                .findByPhone(phone)
                .orElseGet(() -> createAccount(phone));
    }

    private UserAccount createAccount(PhoneNumber phone) {
        UserAccount account = UserAccount.create(UserId.generate(), phone);
        return userAccountRepository.save(account);
    }
}