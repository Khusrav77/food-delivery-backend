package com.shh.foodeliverybackendapp.modules.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MenuItemImageRequest(
        @NotNull
        UUID menuItemId,

        @NotBlank
        String url) {

}
