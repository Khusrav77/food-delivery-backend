package com.marketplace.identity.domain.model;


import java.util.Objects;

public record PhoneNumber(String value)  {

    public PhoneNumber{
        Objects.requireNonNull(value, "Phone number cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be blank");
        }
    }
}
