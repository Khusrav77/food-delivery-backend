package com.shh.foodeliverybackendapp.modules.auth.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserVerifyResponse user

)
{}