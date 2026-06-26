package com.shh.foodeliverybackendapp.modules.menu.dto.request;

import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemSizeRequest(

        @NotNull
        UUID menuItemId,

        @NotNull
        SizeLabel label,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal sizeValue,

        @NotNull
        SizeUnit sizeUnit,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price

) {}