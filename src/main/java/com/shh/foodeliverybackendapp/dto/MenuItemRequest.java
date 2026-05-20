package com.shh.foodeliverybackendapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public final class MenuItemRequest {

    @NotNull
    private UUID productId;

    @NotBlank
    @Size(max = 20)
    private String name;

    private boolean active = true;

    private Integer position = 0;

    public MenuItemRequest() {}

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
