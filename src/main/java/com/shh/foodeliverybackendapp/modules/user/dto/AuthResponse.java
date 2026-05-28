package com.shh.foodeliverybackendapp.modules.user.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserVerifyResponse user

) {}