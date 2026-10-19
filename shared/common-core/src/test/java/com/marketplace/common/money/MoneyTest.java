package com.marketplace.common.money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money first = new Money(new BigDecimal("10.50"), "EUR");
        Money second = new Money(new BigDecimal("5.25"), "EUR");

        Money result = first.add(second);

        assertEquals(new BigDecimal("15.75"), result.amount());
        assertEquals("EUR", result.currency());
    }

    @Test
    void shouldRejectDifferentCurrencies() {
        Money euro = new Money(new BigDecimal("10.00"), "EUR");
        Money dollar = new Money(new BigDecimal("10.00"), "USD");

        assertThrows(
                IllegalArgumentException.class,
                () -> euro.add(dollar)
        );
    }

    @Test
    void shouldMultiplyMoney() {
        Money price = new Money(new BigDecimal("12.50"), "EUR");

        Money result = price.multiply(3);

        assertEquals(new BigDecimal("37.50"), result.amount());
    }

    @Test
    void shouldRejectNegativeResult() {
        Money first = new Money(new BigDecimal("10.00"), "EUR");
        Money second = new Money(new BigDecimal("15.00"), "EUR");

        assertThrows(
                IllegalArgumentException.class,
                () -> first.subtract(second)
        );
    }

    @Test
    void shouldNormalizeCurrency() {
        Money money = new Money(new BigDecimal("10.00"), "eur");

        assertEquals("EUR", money.currency());
    }
}