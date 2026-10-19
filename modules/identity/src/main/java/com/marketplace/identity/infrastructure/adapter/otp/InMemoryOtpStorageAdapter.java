package com.marketplace.identity.infrastructure.adapter.otp;

import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;

import java.util.Objects;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryOtpStorageAdapter implements OtpStoragePort {

    private final ConcurrentMap<String, OtpCode> storage =
            new ConcurrentHashMap<>();

    @Override
    public void save(PhoneNumber phone, OtpCode otpCode) {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(otpCode, "otpCode must not be null");
        storage.put(phone.value(), otpCode);
    }

    @Override
    public boolean verify(PhoneNumber phone, String candidate, Instant now) {
        Objects.requireNonNull(phone, "phone must not be null");
        Objects.requireNonNull(candidate, "candidate must not be null");
        Objects.requireNonNull(now, "now must not be null");
        boolean[] verified = {false};
        storage.computeIfPresent(phone.value(), (key, otpCode) -> {
            verified[0] = otpCode.verify(candidate, now);
            return otpCode;
        });
        return verified[0];
    }

    public Optional<OtpCode> findByPhone(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");
        return Optional.ofNullable(storage.get(phone.value()));
    }

    public void deleteByPhone(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");
        storage.remove(phone.value());
    }
}
