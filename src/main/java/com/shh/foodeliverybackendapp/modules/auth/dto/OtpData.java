package com.shh.foodeliverybackendapp.modules.auth.dto;

import java.time.LocalDateTime;

public record OtpData(
        String code,
        LocalDateTime expiresAt) {
}

