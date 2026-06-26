package com.shh.foodeliverybackendapp.modules.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

    @NotBlank
    @Size(max = 20)
    String name,

    @Size(max = 200)
    String imageUrl,

    Integer position
){}
