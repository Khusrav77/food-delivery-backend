package com.shh.foodeliverybackendapp.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for obtaining a new access token")
public record TokenRefreshRequest(

        @Schema(
                description = "Valid JWT refresh token",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAifQ.signature")
        @NotBlank(message = "Refresh token is required")
        String refreshToken

) {}