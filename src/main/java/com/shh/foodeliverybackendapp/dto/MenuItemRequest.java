package com.shh.foodeliverybackendapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;
public record MenuItemRequest(

        @NotNull
        UUID productId,

        @NotBlank
        @Size(max = 20)
        String name,

        Boolean active,

        Integer position

) {}
