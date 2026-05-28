package com.shh.foodeliverybackendapp.modules.user.dto;

public record VerifyCodeRequest(
        String phone,
        String code
) {}