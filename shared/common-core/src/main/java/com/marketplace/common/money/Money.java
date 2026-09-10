package com.marketplace.common.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(
        BigDecimal amount,
        String currency
) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount must have at most 2 decimal places");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be blank");
        }
        currency = currency.toUpperCase();
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO.setScale(2), currency);
    }

    public Money add(Money other) {
        checkCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);

        BigDecimal result = amount.subtract(other.amount);

        if (result.signum() < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }

        return new Money(result, currency);
    }

    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier cannot be negative");
        }

        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    private void checkCurrency(Money other) {
        Objects.requireNonNull(other, "other must not be null");

        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Currencies must be the same: "
                            + currency + " and " + other.currency);
        }
    }
}