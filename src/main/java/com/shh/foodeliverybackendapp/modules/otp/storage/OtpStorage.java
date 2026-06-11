package com.shh.foodeliverybackendapp.modules.otp.storage;

import java.time.Duration;
import java.util.Optional;

public interface OtpStorage {
    void save(String phone, String code, Duration ttl);

    Optional<String> getCode(String phone);

    void delete(String phone);
}
