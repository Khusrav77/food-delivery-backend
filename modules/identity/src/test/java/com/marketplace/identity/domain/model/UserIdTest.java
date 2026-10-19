package com.marketplace.identity.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserIdTest {

    @Test
    void shouldGenerateUserId() {
        UserId userId = UserId.generate();

        assertNotNull(userId);
        assertNotNull(userId.value());
    }

    @Test
    void shouldCreateUserIdFromUuid() {
        UUID uuid = UUID.randomUUID();

        UserId userId = new UserId(uuid);

        assertEquals(uuid, userId.value());
    }

    @Test
    void shouldRejectNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new UserId(null));
    }
}