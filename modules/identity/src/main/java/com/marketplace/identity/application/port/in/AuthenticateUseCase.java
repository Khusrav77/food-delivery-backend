package com.marketplace.identity.application.port.in;

import com.marketplace.identity.application.command.AuthenticateCommand;
import com.marketplace.identity.application.result.AuthenticationResult;

public interface AuthenticateUseCase extends UseCase<AuthenticateCommand, AuthenticationResult>{
}
