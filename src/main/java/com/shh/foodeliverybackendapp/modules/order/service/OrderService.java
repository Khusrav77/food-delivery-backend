package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.order.dto.AddOrderItemRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderStatus;
import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface OrderService {


        OrderResponse create(OrderRequest request);

        OrderResponse findById(UUID id);

        List<OrderResponse> findAll();

        OrderResponse addItem(UUID orderId, AddOrderItemRequest request);

        OrderResponse removeItem(UUID orderId, UUID orderItemId);

        OrderResponse changeItemQuantity(UUID orderId, UUID orderItemId, Integer quantity);

        OrderResponse changeAddress(UUID orderId, UUID addressId);

        OrderResponse changePaymentMethod(UUID orderId, PaymentMethod paymentMethod);

        OrderResponse changeStatus(UUID orderId, OrderStatus status);

        void cancel(UUID orderId);

}
