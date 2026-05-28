package com.shh.foodeliverybackendapp.modules.menu.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record MenuItemResponse(

        UUID id,
        UUID productId,
        String name,
        boolean active,
        Integer position,
        Instant createdAt,
        Instant updatedAt,
        List<String> imageUrls,
        List<SizeView> sizes,
        Set<String> tagLabels

) {

    public record SizeView(

            UUID id,
            String label,
            BigDecimal sizeValue,
            String sizeUnit,
            BigDecimal price
    ) {}
}