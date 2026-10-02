package com.marketplace.identity.infrastructure.otp;

import com.marketplace.identity.application.port.out.OtpStoragePort;
import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;

import java.util.Objects;
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
    public Optional<OtpCode> findByPhone(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");
        return Optional.ofNullable(storage.get(phone.value()));
    }

    @Override
    public void deleteByPhone(PhoneNumber phone) {
        Objects.requireNonNull(phone, "phone must not be null");
        storage.remove(phone.value());
    }
}