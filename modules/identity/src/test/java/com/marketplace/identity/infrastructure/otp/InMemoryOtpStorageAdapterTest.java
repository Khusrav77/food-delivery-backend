package com.marketplace.identity.infrastructure.otp;

import com.marketplace.identity.domain.model.OtpCode;
import com.marketplace.identity.domain.model.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOtpStorageAdapterTest {

    private static final PhoneNumber PHONE = new PhoneNumber("+79991234567");
    private static final Instant EXPIRES_AT = Instant.parse("2026-01-01T10:03:00Z");

    @Test
    void shouldSaveAndFindOtpByPhone() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();
        OtpCode otpCode = OtpCode.create("123456", EXPIRES_AT);

        storage.save(PHONE, otpCode);
        Optional<OtpCode> result = storage.findByPhone(PHONE);

        assertTrue(result.isPresent());
        assertSame(otpCode, result.get());
    }

    @Test
    void shouldReturnEmptyWhenOtpDoesNotExist() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        Optional<OtpCode> result = storage.findByPhone(PHONE);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReplaceExistingOtpForSamePhone() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        OtpCode firstOtp = OtpCode.create("123456", EXPIRES_AT);

        OtpCode secondOtp = OtpCode.create("654321", EXPIRES_AT);

        storage.save(PHONE, firstOtp);
        storage.save(PHONE, secondOtp);

        Optional<OtpCode> result = storage.findByPhone(PHONE);

        assertTrue(result.isPresent());
        assertSame(secondOtp, result.get());
    }

    @Test
    void shouldDeleteOtpByPhone() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        OtpCode otpCode = OtpCode.create("123456", EXPIRES_AT);

        storage.save(PHONE, otpCode);

        storage.deleteByPhone(PHONE);

        assertTrue(storage.findByPhone(PHONE).isEmpty());
    }

    @Test
    void shouldRejectNullPhoneWhenSaving() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        OtpCode otpCode = OtpCode.create("123456", EXPIRES_AT);

        assertThrows(NullPointerException.class, () -> storage.save(null, otpCode));
    }

    @Test
    void shouldRejectNullOtpWhenSaving() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        assertThrows(NullPointerException.class, () -> storage.save(PHONE, null)
        );
    }

    @Test
    void shouldRejectNullPhoneWhenFinding() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        assertThrows(NullPointerException.class, () -> storage.findByPhone(null));
    }

    @Test
    void shouldRejectNullPhoneWhenDeleting() {
        InMemoryOtpStorageAdapter storage = new InMemoryOtpStorageAdapter();

        assertThrows(NullPointerException.class, () -> storage.deleteByPhone(null));
    }
}
