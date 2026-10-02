package com.marketplace.identity.application.port.in;


import com.marketplace.identity.application.command.CreateUserAccountCommand;
import com.marketplace.identity.domain.model.UserAccount;

public interface CreateUserAccountUseCase extends UseCase <CreateUserAccountCommand, UserAccount> {

}



