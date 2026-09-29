package com.marketplace.identity.application;

import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUserAccountUCTest {

    @Test
    void shouldCreateNewAccountWhenPhoneDoesNotExist() {
        FakeUserAccountRepository repository = new FakeUserAccountRepository();
        CreateUserAccountUC service = new CreateUserAccountUC(repository);
        PhoneNumber phone = new PhoneNumber("+79991234567");

        UserAccount account = service.execute(phone);

        assertNotNull(account);
        assertEquals(phone, account.phone());
        assertEquals(account, repository.findByPhone(phone).orElseThrow());
    }

    @Test
    void shouldReturnExistingAccountWhenPhoneAlreadyExists() {
        FakeUserAccountRepository repository = new FakeUserAccountRepository();
        CreateUserAccountUC service = new CreateUserAccountUC(repository);
        PhoneNumber phone = new PhoneNumber("+79991234567");

        UserAccount existingAccount = UserAccount.create(com.marketplace.identity.domain.model
                                .UserId.generate(), phone);

        repository.save(existingAccount);

        UserAccount result = service.execute(phone);

        assertEquals(existingAccount, result);
    }

    private static class FakeUserAccountRepository implements UserAccountRepository {

        private final Map<String, UserAccount> accounts = new HashMap<>();

        @Override
        public Optional<UserAccount> findById(com.marketplace.identity.domain.model.UserId id) {
            return accounts.values().stream()
                    .filter(account -> account.id().equals(id))
                    .findFirst();
        }

        @Override
        public Optional<UserAccount> findByPhone(PhoneNumber phone) {
            return Optional.ofNullable(accounts.get(phone.value()));
        }

        @Override
        public UserAccount save(UserAccount account) {
            accounts.put(account.phone().value(), account);
            return account;
        }
    }
}
