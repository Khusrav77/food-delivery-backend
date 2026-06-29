package com.shh.foodeliverybackendapp.modules.order.mappeer;

import com.shh.foodeliverybackendapp.modules.base.AbstractMapper;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;

public final class OrderMapper implements AbstractMapper<Order, OrderRequest, OrderResponse> {
    @Override
    public OrderResponse toDto(Order entity, OrderResponse dto) {
        return null;
    }

    @Override
    public Order toEntity(Order entity, OrderRequest dto) {
        return null;
    }

    @Override
    public OrderResponse updateDto(Order entity, OrderRequest dto) {
        return null;
    }
}
