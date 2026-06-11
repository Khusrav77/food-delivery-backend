package com.shh.foodeliverybackendapp.modules.auth.dto;

public record VerifyOtpRequest(
        String phone,
        String code
)
{}