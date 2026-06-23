package com.shh.foodeliverybackendapp.modules.menu.dto.response;

import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeUnit;

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