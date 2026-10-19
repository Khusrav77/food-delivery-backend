package com.marketplace.identity.application.port.in;

import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.result.RefreshTokenResult;

public interface RefreshTokenUseCase extends  UseCase<RefreshTokenCommand, RefreshTokenResult> {
}
