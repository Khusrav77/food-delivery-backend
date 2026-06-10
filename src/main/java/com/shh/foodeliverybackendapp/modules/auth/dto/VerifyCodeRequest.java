package com.shh.foodeliverybackendapp.modules.auth.dto;

public record VerifyCodeRequest(
        String phone,
        String code
) {}