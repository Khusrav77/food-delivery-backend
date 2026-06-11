package com.shh.foodeliverybackendapp.modules.otp.storage;

import com.shh.foodeliverybackendapp.modules.auth.dto.OtpData;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryOtpStorage implements OtpStorage {

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    @Override
    public void save(String phone, String code, Duration ttl) {

        otpStorage.put(phone, new OtpData(code, LocalDateTime.now().plus(ttl)));
    }

    @Override
    public Optional<String> getCode(String phone) {

        OtpData otpData = otpStorage.get(phone);
        if (otpData == null) {
            return Optional.empty();
        }
        if (otpData.expiresAt().isBefore(LocalDateTime.now())) {
            otpStorage.remove(phone);
            return Optional.empty();
        }

        return Optional.of(otpData.code());
    }

    @Override
    public void delete(String phone) {
        otpStorage.remove(phone);
    }
}
