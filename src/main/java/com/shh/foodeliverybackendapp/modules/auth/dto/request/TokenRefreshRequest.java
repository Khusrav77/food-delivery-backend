package com.shh.foodeliverybackendapp.modules.auth.dto.request;

public record TokenRefreshRequest(
        String refreshToken
)
{}