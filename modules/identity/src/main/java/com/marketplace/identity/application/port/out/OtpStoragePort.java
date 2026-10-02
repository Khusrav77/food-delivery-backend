package com.marketplace.identity.application.port.out;

import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;


import java.util.Optional;

public interface OtpStoragePort {
    void save(PhoneNumber phone, OtpCode otp);
    Optional<OtpCode> findByPhone(PhoneNumber phone);
    void deleteByPhone(PhoneNumber phone);
}
