package com.marketplace.identity.infrastructure.user;

import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class InMemoryUserAccountAdapter implements UserAccountPort {

    private final Map<PhoneNumber, UserAccount> accountsByPhone = new ConcurrentHashMap<>();
    private final Map<UserId, UserAccount> accountsById = new ConcurrentHashMap<>();

    @Override
    public Optional<UserAccount> findByPhone(PhoneNumber phone) {
        return Optional.ofNullable(accountsByPhone.get(phone));
    }

    @Override
    public Optional<UserAccount> findById(UserId userId) {
        return Optional.ofNullable(accountsById.get(userId));
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        accountsByPhone.put(userAccount.phone(), userAccount);
        accountsById.put(userAccount.id(), userAccount);
        return userAccount;
    }
}