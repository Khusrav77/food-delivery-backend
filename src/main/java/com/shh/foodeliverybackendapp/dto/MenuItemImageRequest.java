package com.shh.foodeliverybackendapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MenuItemImageRequest(
        @NotNull
        UUID menuItemId,

        @NotBlank
        String url) {

}
