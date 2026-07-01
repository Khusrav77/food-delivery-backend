package com.shh.foodeliverybackendapp.modules.order.dto;

import com.shh.foodeliverybackendapp.modules.order.entity.OrderStatus;
import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        UUID id,
        UUID userId,
        UUID addressId,
        OrderStatus status,
        PaymentMethod paymentMethod,
        BigDecimal deliveryPrice,
        BigDecimal totalPrice,
        String comment,
        Instant createdAt,
        List<OrderItemResponse> items
) {}
