package com.marketplace.identity.api;

import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.port.in.RequestOtpUseCase;
import com.marketplace.identity.application.port.in.VerifyOtpUseCase;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.api.dto.RequestOtpRequest;
import com.marketplace.identity.api.dto.RequestOtpResponse;
import com.marketplace.identity.api.dto.VerifyOtpRequest;
import com.marketplace.identity.api.dto.VerifyOtpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public final class AuthController {

    private final RequestOtpUseCase requestOtpUseCase;
    private final VerifyOtpUseCase verifyOtpUseCase;

    public AuthController(
            RequestOtpUseCase requestOtpUseCase,
            VerifyOtpUseCase verifyOtpUseCase
    ) {
        this.requestOtpUseCase = Objects.requireNonNull(
                requestOtpUseCase, "requestOtpUseCase must not be null");

        this.verifyOtpUseCase = Objects.requireNonNull(
                verifyOtpUseCase, "verifyOtpUseCase must not be null");
    }

    @PostMapping("/otp/request")
    public ResponseEntity<RequestOtpResponse> requestOtp(
            @RequestBody RequestOtpRequest request) {
        RequestOtpCommand command = new RequestOtpCommand(new PhoneNumber(request.phone()));
        RequestOtpResult result = requestOtpUseCase.execute(command);

        return ResponseEntity.ok(new RequestOtpResponse(result.expiresInSeconds()));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @RequestBody VerifyOtpRequest request) {
        VerifyOtpCommand command = new VerifyOtpCommand(new PhoneNumber(request.phone()), request.code());
        VerifyOtpResult result = verifyOtpUseCase.execute(command);
        UUID userId = result.id().value();

        return ResponseEntity.ok(new VerifyOtpResponse(userId));
    }
}