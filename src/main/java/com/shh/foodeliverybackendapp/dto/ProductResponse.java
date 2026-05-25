package com.shh.foodeliverybackendapp.dto;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(

        UUID id,
        UUID categoryId,
        String name,
        String description,
        boolean active,
        Integer position,
        Instant createdAt,
        Instant updatedAt

) {}