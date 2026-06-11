package com.shh.foodeliverybackendapp.modules.menu.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MenuItemImageResponse(
        UUID id,
        UUID menuItemId,
        String url,
        Integer position,
        Instant createdAt,
        Instant updatedAt
) {}
