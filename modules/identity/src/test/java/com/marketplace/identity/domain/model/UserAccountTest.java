package com.marketplace.identity.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserAccountTest {

    @Test
    void shouldCreateActiveAccount() {
        UserId userId = UserId.generate();

        UserAccount account = UserAccount.create(userId);

        assertEquals(userId, account.id());
        assertEquals(AccountStatus.ACTIVE, account.status());
    }

    @Test
    void shouldBlockActiveAccount() {
        UserAccount account = UserAccount.create(UserId.generate());

        account.block();

        assertEquals(AccountStatus.BLOCKED, account.status());
    }

    @Test
    void shouldActivateBlockedAccount() {
        UserAccount account = UserAccount.create(UserId.generate());

        account.block();
        account.activate();

        assertEquals(AccountStatus.ACTIVE, account.status());
    }

    @Test
    void shouldDisableAccount() {
        UserAccount account = UserAccount.create(UserId.generate());

        account.disable();

        assertEquals(AccountStatus.DISABLED, account.status());
    }

    @Test
    void shouldNotActivateDisabledAccount() {
        UserAccount account = UserAccount.create(UserId.generate());

        account.disable();

        assertThrows(
                IllegalStateException.class,
                account::activate);
    }

    @Test
    void shouldNotBlockDisabledAccount() {
        UserAccount account = UserAccount.create(UserId.generate());

        account.disable();

        assertThrows(
                IllegalStateException.class,
                account::block);
    }
}
