package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.domain.event.UserAccountCreated;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.EventPublisherPort;
import com.marketplace.identity.application.port.out.UserAccountPort;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.domain.model.UserAccount;
import com.marketplace.identity.domain.model.UserId;

import java.util.Objects;

public final class CreateUserAccountService implements CreateUserAccountUseCase {

    private final UserAccountPort userAccountPort;
    private final EventPublisherPort eventPublisherPort;
    private final ClockPort clockPort;

    public CreateUserAccountService(
            UserAccountPort userAccountPort,
            EventPublisherPort eventPublisherPort,
            ClockPort clockPort
    ) {
        this.userAccountPort = Objects.requireNonNull(userAccountPort);
        this.eventPublisherPort = Objects.requireNonNull(eventPublisherPort);
        this.clockPort = Objects.requireNonNull(clockPort);
    }

    public UserAccount execute(CreateUserAccountCommand command) {
        Objects.requireNonNull(command, "phone must not be null");

        return userAccountPort
                .findByPhone(command.phone())
                .orElseGet(() -> createAccount(command.phone()));
    }

    private UserAccount createAccount(PhoneNumber phone) {
        UserAccount account = UserAccount.create(UserId.generate(), phone);
        UserAccount saved = userAccountPort.save(account);

        eventPublisherPort.publish(
                new UserAccountCreated(saved.id(), saved.phone(), clockPort.now()));

        return saved;
    }
}