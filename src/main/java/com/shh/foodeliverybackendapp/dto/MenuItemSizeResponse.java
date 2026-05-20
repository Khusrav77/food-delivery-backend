package com.shh.foodeliverybackendapp.dto;

import com.shh.foodeliverybackendapp.entity.product.SizeLabel;
import com.shh.foodeliverybackendapp.entity.product.SizeUnit;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemSizeResponse(

        UUID id,
        UUID menuItemId,
        String menuItemName,
        SizeLabel label,
        BigDecimal sizeValue,
        SizeUnit sizeUnit,
        BigDecimal price

) {}