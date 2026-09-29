package com.marketplace.identity.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAccountTest {

    @Test
    void shouldCreateActiveAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserId userId = UserId.generate();

        UserAccount account = UserAccount.create(userId, phone);

        assertEquals(userId, account.id());
        assertEquals(AccountStatus.ACTIVE, account.status());

        assertTrue(account.isActive());
        assertFalse(account.isBlocked());
        assertFalse(account.isDisabled());
    }

    @Test
    void shouldBlockActiveAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.block();

        assertEquals(AccountStatus.BLOCKED, account.status());

        assertFalse(account.isActive());
        assertTrue(account.isBlocked());
        assertFalse(account.isDisabled());
    }

    @Test
    void shouldActivateBlockedAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.block();
        account.activate();

        assertEquals(AccountStatus.ACTIVE, account.status());

        assertTrue(account.isActive());
        assertFalse(account.isBlocked());
        assertFalse(account.isDisabled());
    }

    @Test
    void shouldDisableActiveAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.disable();

        assertEquals(AccountStatus.DISABLED, account.status());

        assertFalse(account.isActive());
        assertFalse(account.isBlocked());
        assertTrue(account.isDisabled());
    }

    @Test
    void shouldDisableBlockedAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.block();
        account.disable();

        assertEquals(AccountStatus.DISABLED, account.status());

        assertTrue(account.isDisabled());
    }

    @Test
    void shouldNotActivateDisabledAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.disable();

        assertThrows(IllegalStateException.class, account::activate);

        assertTrue(account.isDisabled());
    }

    @Test
    void shouldNotBlockDisabledAccount() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount account = UserAccount.create(UserId.generate(), phone);

        account.disable();

        assertThrows(IllegalStateException.class, account::block);

        assertTrue(account.isDisabled());
    }

    @Test
    void shouldCreateAccountWithPhone() {
        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserId userId = UserId.generate();

        UserAccount account = UserAccount.create(userId, phone);

        assertEquals(userId, account.id());
        assertEquals(phone, account.phone());
        assertEquals(AccountStatus.ACTIVE, account.status());
    }

    @Test
    void shouldRejectNullUserId() {
        PhoneNumber phone = new PhoneNumber("+79991234567");

        assertThrows(NullPointerException.class, () -> UserAccount.create(null, phone));
    }

    @Test
    void shouldRejectNullPhone() {
        UserId userId = UserId.generate();

        assertThrows(NullPointerException.class, () -> UserAccount.create(userId, null));
    }
}