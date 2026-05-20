package com.shh.foodeliverybackendapp.dto;
import java.time.Instant;
import java.util.UUID;

public final class CategoryResponse {
    private final UUID id;
    private final String name;
    private final String imageUrl;
    private final Integer position;
    private final Instant createdAt;
    private final Instant updatedAt;

    public CategoryResponse(UUID id, String name, String imageUrl,
                            Integer position, Instant createdAt,
                            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {return id;}

    public String getName() {return name;}

    public String getImageUrl() {return imageUrl;}

    public Integer getPosition() {return position;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getUpdatedAt() {return updatedAt;}
}
