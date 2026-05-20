package com.shh.foodeliverybackendapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public final class ProductRequest {

    @NotNull
    private UUID categoryId;

    @NotBlank
    @Size(max = 20)
    private String name;

    @Size(max = 2000)
    private String description;

    private Boolean active = true;

    private Integer position = 0;

    public ProductRequest() {}

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
