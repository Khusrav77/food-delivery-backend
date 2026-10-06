package com.marketplace.identity.api.controller;

import com.marketplace.identity.api.dto.*;
import com.marketplace.identity.api.mapper.AuthMapper;
import com.marketplace.identity.application.command.AuthenticateCommand;
import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.port.in.AuthenticateUseCase;
import com.marketplace.identity.application.port.in.RefreshTokenUseCase;
import com.marketplace.identity.application.port.in.RequestOtpUseCase;
import com.marketplace.identity.application.result.AuthenticationResult;
import com.marketplace.identity.application.result.RefreshTokenResult;
import com.marketplace.identity.application.result.RequestOtpResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public final class AuthController {

    private final RequestOtpUseCase requestOtpUseCase;
    private final AuthenticateUseCase authenticateUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(
            RequestOtpUseCase requestOtpUseCase,
            AuthenticateUseCase authenticateUseCase,
            RefreshTokenUseCase refreshTokenUseCase
    ) {
        this.requestOtpUseCase = Objects.requireNonNull(
                requestOtpUseCase, "requestOtpUseCase must not be null");
        this.authenticateUseCase = Objects.requireNonNull(
                authenticateUseCase, "authenticateUseCase must not be null");
        this.refreshTokenUseCase = Objects.requireNonNull(
                refreshTokenUseCase, "refreshTokenUseCase must not be null");
    }

    @PostMapping("/otp/request")
    public ResponseEntity<OtpResponse> requestOtp(
            @RequestBody OtpRequest request) {
        RequestOtpCommand command = AuthMapper.toCommand(request);
        RequestOtpResult result = requestOtpUseCase.execute(command);
        return ResponseEntity.ok(AuthMapper.toResponse(result));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthenticateCommand command = AuthMapper.toCommand(request);
        AuthenticationResult result = authenticateUseCase.execute(command);
        return ResponseEntity.ok(AuthMapper.toResponse(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @RequestBody RefreshTokenRequest request) {
        RefreshTokenCommand command = AuthMapper.toCommand(request);
        RefreshTokenResult result = refreshTokenUseCase.execute(command);
        return ResponseEntity.ok(AuthMapper.toResponse(result));
    }
}