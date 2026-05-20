package com.shh.foodeliverybackendapp.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class MenuItemResponse {

    private final UUID id;
    private final UUID productId;
    private final String name;
    private final Boolean active;
    private final Integer position;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<String> imageUrls;
    private final List<SizeView> sizes;
    private final Set<String> tagLabels;

    public MenuItemResponse(UUID id,
                            UUID productId,
                            String name,
                            Boolean active,
                            Integer position,
                            Instant createdAt,
                            Instant updatedAt,
                            List<String> imageUrls,
                            List<SizeView> sizes,
                            Set<String> tagLabels) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.active = active;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrls = imageUrls;
        this.sizes = sizes;
        this.tagLabels = tagLabels;
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getName() { return name; }
    public Boolean getActive() { return active; }
    public Integer getPosition() { return position; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<String> getImageUrls() { return imageUrls; }
    public List<SizeView> getSizes() { return sizes; }
    public Set<String> getTagLabels() { return tagLabels; }

    /** Nested view of a single size variant of a menu item. */
    public record SizeView(UUID id,
                           String label,
                           BigDecimal sizeValue,
                           String sizeUnit,
                           BigDecimal price) {}
}
