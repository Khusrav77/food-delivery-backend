package com.shh.foodeliverybackendapp.dto;

import java.time.Instant;
import java.util.UUID;

public final class ProductResponse {

    private final UUID id;
    private final UUID categoryId;
    private final String name;
    private final String description;
    private final Boolean active;
    private final Integer position;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ProductResponse(UUID id,
                           UUID categoryId,
                           String name,
                           String description,
                           Boolean active,
                           Integer position,
                           Instant createdAt,
                           Instant updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.active = active;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Boolean getActive() { return active; }
    public Integer getPosition() { return position; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
