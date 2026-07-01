package com.shh.foodeliverybackendapp.modules.order.dto;

import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeUnit;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productItemId,
        UUID productItemSizeId,
        String productName,
        String productImage,
        SizeLabel sizeLabel,
        BigDecimal sizeValue,
        SizeUnit sizeUnit,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal

) { }