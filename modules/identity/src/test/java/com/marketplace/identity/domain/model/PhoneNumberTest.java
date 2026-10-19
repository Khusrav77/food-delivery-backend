package com.marketplace.identity.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNumberTest {

    @Test
    void shouldCreatePhoneNumber() {
        PhoneNumber phone = new PhoneNumber("+79991234567");

        assertEquals("+79991234567", phone.value());
    }

    @Test
    void shouldRejectNullPhoneNumber() {
        assertThrows(NullPointerException.class, () -> new PhoneNumber(null));
    }

    @Test
    void shouldRejectBlankPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(" "));
    }

    @Test
    void shouldRejectEmptyPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(""));
    }
}
