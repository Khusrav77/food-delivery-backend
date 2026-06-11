package com.shh.foodeliverybackendapp.modules.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProductRequest(

        @NotNull
        UUID categoryId,

        @NotBlank
        @Size(max = 20)
        String name,

        @Size(max = 2000)
        String description,
        Boolean active,
        Integer position

) {}