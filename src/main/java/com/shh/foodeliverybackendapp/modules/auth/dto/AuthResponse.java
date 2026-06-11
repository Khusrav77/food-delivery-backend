package com.shh.foodeliverybackendapp.modules.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserVerifyResponse user

)
{}