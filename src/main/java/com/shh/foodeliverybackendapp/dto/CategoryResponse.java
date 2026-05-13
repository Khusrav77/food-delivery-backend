package com.shh.foodeliverybackendapp.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CategoryResponse {
    private UUID id;
    private String name;
    private String imageUrl;
    private Integer position = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponse(UUID id, String name, String imageUrl,
                            Integer position, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
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

    public LocalDateTime getCreatedAt() {return createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
}
