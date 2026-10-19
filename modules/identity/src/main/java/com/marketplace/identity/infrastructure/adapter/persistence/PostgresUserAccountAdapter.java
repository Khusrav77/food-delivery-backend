package com.marketplace.identity.infrastructure.adapter.persistence;

import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostgresUserAccountAdapter implements UserAccountPort {

    private final UserAccountJpaRepository repository;

    public PostgresUserAccountAdapter(UserAccountJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findByPhone(PhoneNumber phone) {
        return repository.findByPhone(phone.value()).map(UserAccountEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findById(UserId userId) {
        return repository.findById(userId.value()).map(UserAccountEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public UserAccount save(UserAccount userAccount) {
        UserAccountEntity saved = repository.save(UserAccountEntityMapper.toEntity(userAccount));
        return UserAccountEntityMapper.toDomain(saved);
    }
}
