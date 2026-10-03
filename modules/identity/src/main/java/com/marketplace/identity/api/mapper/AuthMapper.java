package com.marketplace.identity.api.mapper;

import com.marketplace.identity.application.command.RequestOtpCommand;
import com.marketplace.identity.application.command.VerifyOtpCommand;
import com.marketplace.identity.application.result.RequestOtpResult;
import com.marketplace.identity.application.result.VerifyOtpResult;
import com.marketplace.identity.domain.model.PhoneNumber;
import com.marketplace.identity.api.dto.OtpRequest;
import com.marketplace.identity.api.dto.OtpResponse;
import com.marketplace.identity.api.dto.VerifyOtpRequest;
import com.marketplace.identity.api.dto.VerifyOtpResponse;

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

    public static VerifyOtpCommand toCommand(VerifyOtpRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        return new VerifyOtpCommand(new PhoneNumber(
                request.phone()),
                request.code());
    }

    public static VerifyOtpResponse toResponse(VerifyOtpResult result) {
        Objects.requireNonNull(result, "result must not be null");
        return new VerifyOtpResponse(result.id().value());
    }
}
