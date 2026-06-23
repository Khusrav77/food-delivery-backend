package com.shh.foodeliverybackendapp.modules.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication result containing JWT tokens and user information")
public record AuthResponse(

        @Schema(
                description = "JWT access token used to authorize API requests",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJyb2xlIjoiVVNFUiJ9.signature")
        String accessToken,

        @Schema(
                description = "JWT refresh token used to obtain a new access token",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAifQ.signature")
        String refreshToken,

        @Schema(description = "Authenticated user information")
        UserVerifyResponse user

) { }