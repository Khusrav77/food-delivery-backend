package com.shh.foodeliverybackendapp.modules.order.controller;

import com.shh.foodeliverybackendapp.modules.order.dto.AddOrderItemRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderStatus;
import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import com.shh.foodeliverybackendapp.modules.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse create(@RequestBody @Valid OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderService.findAll();
    }

    @PostMapping("/{orderId}/items")
    public OrderResponse addItem(@PathVariable UUID orderId,
                                 @RequestBody @Valid AddOrderItemRequest request) {

        return orderService.addItem(orderId, request);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}/quantity")
    public OrderResponse changeItemQuantity(@PathVariable UUID orderId,
                                            @PathVariable UUID orderItemId,
                                            @RequestParam Integer quantity) {
        return orderService.changeItemQuantity(orderId, orderItemId, quantity);
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public OrderResponse removeItem(@PathVariable UUID orderId,
                                    @PathVariable UUID orderItemId) {
        return orderService.removeItem(orderId, orderItemId);
    }

    @PatchMapping("/{orderId}/address/{addressId}")
    public OrderResponse changeAddress(@PathVariable UUID orderId,
                                       @PathVariable UUID addressId) {
        return orderService.changeAddress(orderId, addressId);
    }

    @PatchMapping("/{orderId}/payment")
    public OrderResponse changePaymentMethod(@PathVariable UUID orderId,
                                             @RequestParam PaymentMethod paymentMethod) {
        return orderService.changePaymentMethod(orderId, paymentMethod);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse changeStatus(@PathVariable UUID orderId,
                                      @RequestParam OrderStatus status) {
        return orderService.changeStatus(orderId, status);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable UUID orderId) {
        orderService.cancel(orderId);
    }
}