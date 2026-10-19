package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.LogoutAllSessionsCommand;
import com.marketplace.identity.application.port.in.LogoutAllSessionsUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;

import java.util.Objects;

public final class LogoutAllSessionsService implements LogoutAllSessionsUseCase {

    private final RefreshSessionPort refreshSessionPort;
    private final ClockPort clockPort;

    public LogoutAllSessionsService(
            RefreshSessionPort refreshSessionPort,
            ClockPort clockPort) {
        this.refreshSessionPort = Objects.requireNonNull(
                refreshSessionPort, "refreshSessionPort must not be null");
        this.clockPort = Objects.requireNonNull(clockPort, "clockPort must not be null");
    }

    @Override
    public Void execute(LogoutAllSessionsCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        refreshSessionPort.revokeAll(command.userId(), clockPort.now());
        return null;
    }
}
