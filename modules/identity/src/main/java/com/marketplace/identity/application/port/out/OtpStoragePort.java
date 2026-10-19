package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;

import java.time.Instant;

public interface OtpStoragePort {
    void save(PhoneNumber phone, OtpCode otp);
    boolean verify(PhoneNumber phone, String candidate, Instant now);
}
