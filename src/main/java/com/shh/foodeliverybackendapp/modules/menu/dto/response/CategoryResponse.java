package com.shh.foodeliverybackendapp.modules.menu.dto.response;
import java.time.Instant;
import java.util.UUID;

public record CategoryResponse (
        UUID id,
        String name,
        String imageUrl,
        Integer position,
        Instant createdAt,
        Instant updatedAt

){}



