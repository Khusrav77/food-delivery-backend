package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.EventPublisherPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.domain.event.UserAccountCreated;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.event.DomainEvent;
import com.marketplace.identity.infrastructure.event.InMemoryEventPublisherAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserAccountServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @Test
    void shouldCreateNewAccountAndPublishEvent() {

        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        InMemoryEventPublisherAdapter inMemoryEventPublisherAdapter = new InMemoryEventPublisherAdapter();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        CreateUserAccountService service =
                new CreateUserAccountService(
                        userAccountPort,
                        inMemoryEventPublisherAdapter,
                        clockPort);

        PhoneNumber phone = new PhoneNumber("+79991234567");
        CreateUserAccountCommand command = new CreateUserAccountCommand(phone);
        UserAccount account = service.execute(command);

        assertNotNull(account);
        assertEquals(phone, account.phone());

        assertEquals(account, userAccountPort.findByPhone(phone).orElseThrow());
        assertEquals(1, inMemoryEventPublisherAdapter.events().size());

        DomainEvent event = inMemoryEventPublisherAdapter.events().getFirst();

        assertInstanceOf(UserAccountCreated.class, event);

        UserAccountCreated created = (UserAccountCreated) event;

        assertEquals(account.id(), created.userId());
        assertEquals(phone, created.phone());
        assertEquals(NOW, created.occurredAt());
    }

    @Test
    void shouldReturnExistingAccountWithoutPublishingEvent() {

        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        InMemoryEventPublisherAdapter inMemoryEventPublisherAdapter = new InMemoryEventPublisherAdapter();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        CreateUserAccountService service =
                new CreateUserAccountService(
                        userAccountPort,
                        inMemoryEventPublisherAdapter,
                        clockPort);

        PhoneNumber phone = new PhoneNumber("+79991234567");
        UserAccount existingAccount = UserAccount.create(UserId.generate(), phone);
        userAccountPort.save(existingAccount);
        CreateUserAccountCommand command = new CreateUserAccountCommand(phone);
        UserAccount result = service.execute(command);

        assertEquals(existingAccount, result);
        assertTrue(inMemoryEventPublisherAdapter.events().isEmpty());
    }

    @Test
    void shouldRejectNullCommand() {

        FakeUserAccountPort userAccountPort = new FakeUserAccountPort();
        InMemoryEventPublisherAdapter inMemoryEventPublisherAdapter = new InMemoryEventPublisherAdapter();
        FakeClockPort clockPort = new FakeClockPort(NOW);

        CreateUserAccountService service =
                new CreateUserAccountService(
                        userAccountPort,
                        inMemoryEventPublisherAdapter,
                        clockPort);

        assertThrows(NullPointerException.class, () -> service.execute(null));
    }

    private static class FakeUserAccountPort implements UserAccountPort {

        private final Map<String, UserAccount> accounts = new HashMap<>();

        @Override
        public Optional<UserAccount> findByPhone(PhoneNumber phone) {
            return Optional.ofNullable(
                    accounts.get(phone.value()));
        }

        @Override
        public UserAccount save(UserAccount userAccount) {
            accounts.put(userAccount.phone().value(), userAccount);
            return userAccount;
        }
    }

    private static class FakeClockPort implements ClockPort {

        private final Instant now;

        private FakeClockPort(Instant now) {
            this.now = now;
        }

        @Override
        public Instant now() {
            return now;
        }
    }
}