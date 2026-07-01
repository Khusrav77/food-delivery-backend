package com.shh.foodeliverybackendapp.modules.order.dto;

import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderRequest(
        @NotNull
        UUID addressId,

        String comment,

        @NotNull
        PaymentMethod paymentMethod,

        @NotEmpty
        List<@Valid OrderItemRequest> items
) {}
