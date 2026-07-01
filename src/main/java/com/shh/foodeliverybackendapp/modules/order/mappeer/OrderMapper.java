package com.shh.foodeliverybackendapp.modules.order.mappeer;

import com.shh.foodeliverybackendapp.modules.order.dto.OrderItemResponse;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {

        if (order == null) {return null;}

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getAddress().getId(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getDeliveryPrice(),
                order.getTotalPrice(),
                order.getComment(),
                order.getCreatedAt(),
                mapItems(order));
    }

    private List<OrderItemResponse> mapItems(Order order) {

        return order.getOrderItems()
                .stream()
                .map(this::mapItem)
                .toList();
    }

    private OrderItemResponse mapItem(OrderItem item) {

        return new OrderItemResponse(
                item.getProductItemId(),
                item.getProductItemSizeId(),
                item.getProductName(),
                item.getProductImage(),
                item.getSizeLabel(),
                item.getSizeValue(),
                item.getSizeUnit(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal());
    }
}