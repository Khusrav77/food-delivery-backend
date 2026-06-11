package com.shh.foodeliverybackendapp.modules.auth.dto.request;

public record VerifyOtpRequest(
        String phone,
        String code
)
{}