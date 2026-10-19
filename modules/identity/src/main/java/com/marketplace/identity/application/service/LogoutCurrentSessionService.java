package com.marketplace.identity.application.service;

import com.marketplace.identity.application.command.LogoutCurrentSessionCommand;
import com.marketplace.identity.application.port.in.LogoutCurrentSessionUseCase;
import com.marketplace.identity.application.port.out.ClockPort;
import com.marketplace.identity.application.port.out.RefreshSessionPort;

import java.util.Objects;

public final class LogoutCurrentSessionService implements LogoutCurrentSessionUseCase {

    private final RefreshSessionPort refreshSessionPort;
    private final ClockPort clockPort;

    public LogoutCurrentSessionService(
            RefreshSessionPort refreshSessionPort,
            ClockPort clockPort) {
        this.refreshSessionPort = Objects.requireNonNull(
                refreshSessionPort, "refreshSessionPort must not be null");
        this.clockPort = Objects.requireNonNull(clockPort, "clockPort must not be null");
    }

    @Override
    public Void execute(LogoutCurrentSessionCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        refreshSessionPort.revokeCurrent(
                command.userId(), command.refreshToken(), clockPort.now());
        return null;
    }
}
