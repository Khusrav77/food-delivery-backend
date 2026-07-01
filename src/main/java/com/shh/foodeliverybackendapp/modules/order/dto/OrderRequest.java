package com.shh.foodeliverybackendapp.modules.order.dto;

import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.util.List;
import java.util.UUID;

public record OrderRequest(
        @NotNull
        UUID addressId,

        String comment,

        @NotNull
        PaymentMethod paymentMethod,

        @NotEmpty
        List<@Valid OrderItemRequest> items
) {}
