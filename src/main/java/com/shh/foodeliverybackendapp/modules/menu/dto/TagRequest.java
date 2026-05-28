package com.shh.foodeliverybackendapp.modules.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequest(

        @NotBlank
        @Size(max = 20)
        String name,
        String color

) {}