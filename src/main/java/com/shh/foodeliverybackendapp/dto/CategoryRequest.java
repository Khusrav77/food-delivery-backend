package com.shh.foodeliverybackendapp.dto;

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
