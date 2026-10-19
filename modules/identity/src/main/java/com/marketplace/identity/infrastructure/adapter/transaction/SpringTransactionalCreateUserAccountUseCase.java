package com.marketplace.identity.infrastructure.adapter.transaction;

import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.application.port.in.CreateUserAccountUseCase;
import com.marketplace.identity.application.service.CreateUserAccountService;
import com.marketplace.identity.domain.model.UserAccount;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

public final class SpringTransactionalCreateUserAccountUseCase implements CreateUserAccountUseCase {

    private final CreateUserAccountService delegate;
    private final TransactionTemplate transactions;

    public SpringTransactionalCreateUserAccountUseCase(
            CreateUserAccountService delegate,
            PlatformTransactionManager transactionManager) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager must not be null"));
    }

    @Override
    public UserAccount execute(CreateUserAccountCommand command) {
        return transactions.execute(status -> delegate.execute(command));
    }
}
