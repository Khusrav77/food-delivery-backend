package com.marketplace.identity.api.controller;

import com.marketplace.identity.api.dto.AuthenticatedSessionResponse;
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
import com.marketplace.identity.domain.model.UserId;
import com.marketplace.identity.api.dto.AuthenticatedSessionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public final class AuthController {

    private final RequestOtpUseCase requestOtpUseCase;
    private final AuthenticateUseCase authenticateUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final boolean includeOtpDebugCode;

    public AuthController(
            RequestOtpUseCase requestOtpUseCase,
            AuthenticateUseCase authenticateUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            @Value("${identity.otp.expose-code:false}") boolean includeOtpDebugCode
    ) {
        this.requestOtpUseCase = Objects.requireNonNull(
                requestOtpUseCase, "requestOtpUseCase must not be null");
        this.authenticateUseCase = Objects.requireNonNull(
                authenticateUseCase, "authenticateUseCase must not be null");
        this.refreshTokenUseCase = Objects.requireNonNull(
                refreshTokenUseCase, "refreshTokenUseCase must not be null");
        this.includeOtpDebugCode = includeOtpDebugCode;
    }

    @PostMapping("/otp/request")
    public ResponseEntity<OtpResponse> requestOtp(
            @RequestBody OtpRequest request) {
        RequestOtpCommand command = AuthMapper.toCommand(request);
        RequestOtpResult result = requestOtpUseCase.execute(command);
        return ResponseEntity.ok(AuthMapper.toResponse(result, includeOtpDebugCode));
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

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedSessionResponse> currentSession(
            Authentication authentication) {
        UserId userId = (UserId) authentication.getPrincipal();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toUnmodifiableSet());
        return ResponseEntity.ok(new AuthenticatedSessionResponse(userId.value(), roles));
    }
}
