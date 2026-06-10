package com.shh.foodeliverybackendapp.modules.auth.entity;

import java.time.LocalDateTime;

public record OtpData(
        String code,
        LocalDateTime expiresAt) {
}

