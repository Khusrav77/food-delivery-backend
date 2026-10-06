package com.marketplace.identity.api.mapper;

import com.marketplace.identity.api.dto.*;
import com.marketplace.identity.application.command.AuthenticateCommand;
import com.marketplace.identity.application.command.RefreshTokenCommand;
import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.result.AuthenticationResult;
import com.marketplace.identity.application.result.RefreshTokenResult;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.domain.model.PhoneNumber;

import java.util.Objects;

public final class AuthMapper {

    private AuthMapper() {}

    public static RequestOtpCommand toCommand(OtpRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        return new RequestOtpCommand(new PhoneNumber(request.phone()));
    }

    public static OtpResponse toResponse(RequestOtpResult result) {
        Objects.requireNonNull(result, "result must not be null");
        return new OtpResponse(result.expiresInSeconds());
    }

    public static AuthenticateCommand toCommand(LoginRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        return new AuthenticateCommand(
                new PhoneNumber(request.phone()), request.code());
    }

    public static LoginResponse toResponse(AuthenticationResult result) {
        return new LoginResponse(
                result.accessToken().value(),
                result.accessToken().expiresInSeconds(),
                result.refreshToken().value(),
                result.refreshToken().expiresInSeconds());
    }

    public static RefreshTokenCommand toCommand(RefreshTokenRequest request) {
        return new RefreshTokenCommand(request.refreshToken());
    }

    public static RefreshTokenResponse toResponse(
            RefreshTokenResult result) {
        return new RefreshTokenResponse(
                result.accessToken().value(), result.accessToken().expiresInSeconds());
    }
}
