package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.address.entity.UserAddress;
import com.shh.foodeliverybackendapp.modules.address.service.UserAddressServiceImpl;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemSizeService;
import com.shh.foodeliverybackendapp.modules.order.dto.AddOrderItemRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderItem;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderStatus;
import com.shh.foodeliverybackendapp.modules.order.entity.PaymentMethod;
import com.shh.foodeliverybackendapp.modules.order.mappeer.OrderMapper;
import com.shh.foodeliverybackendapp.modules.order.repository.OrderRepository;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import com.shh.foodeliverybackendapp.modules.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal DELIVERY_PRICE = BigDecimal.valueOf(50);

    private final OrderRepository orderRepo;
    private final UserService userService;
    private final UserAddressServiceImpl addressService;
    private final ProductItemSizeService productItemSizeService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(
            OrderRepository orderRepo,
            UserService userService,
            UserAddressServiceImpl addressService,
            ProductItemSizeService productItemSizeService,
            OrderMapper orderMapper
    ) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.addressService = addressService;
        this.productItemSizeService = productItemSizeService;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse create(OrderRequest request) {

        User user = userService.getCurrentUserEntity();
        UserAddress address = addressService.getAddressById(request.addressId());

        Order order = Order.create(
                user,
                address,
                request.paymentMethod(),
                DELIVERY_PRICE,
                request.comment());

        request.items()
                .stream()
                .map(this::createOrderItem)
                .forEach(item -> {
                    item.setOrder(order);
                    order.getOrderItems().add(item);});

        recalculateOrder(order);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(UUID id) {
        return orderMapper.toResponse(getOrderById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {

        User currentUser = userService.getCurrentUserEntity();

        return orderRepo.findAllByUser(currentUser)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse addItem(UUID orderId, AddOrderItemRequest request) {

        Order order = getOrderById(orderId);
        validateOrderCanBeEdited(order);
        OrderItem item = createOrderItem(request);
        item.setOrder(order);
        order.getOrderItems().add(item);
        recalculateOrder(order);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse removeItem(UUID orderId, UUID orderItemId) {

        Order order = getOrderById(orderId);
        validateOrderCanBeEdited(order);
        OrderItem item = getOrderItem(order, orderItemId);
        order.getOrderItems().remove(item);
        recalculateOrder(order);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse changeItemQuantity(
            UUID orderId,
            UUID orderItemId,
            Integer quantity) {

        Order order = getOrderById(orderId);
        validateOrderCanBeEdited(order);
        OrderItem item = getOrderItem(order, orderItemId);
        item.updateQuantity(quantity);
        recalculateOrder(order);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse changeAddress(UUID orderId, UUID addressId) {

        Order order = getOrderById(orderId);
        validateOrderCanBeEdited(order);
        UserAddress address = addressService.getAddressById(addressId);
        order.setAddress(address);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse changePaymentMethod(UUID orderId, PaymentMethod paymentMethod) {

        Order order = getOrderById(orderId);
        validateOrderCanBeEdited(order);
        order.setPaymentMethod(paymentMethod);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse changeStatus(UUID orderId, OrderStatus status) {

        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public void cancel(UUID orderId) {

        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    private Order getOrderById(UUID id) {

        User currentUser = userService.getCurrentUserEntity();
        return orderRepo.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));
    }

    private OrderItem getOrderItem(Order order, UUID orderItemId) {

        return order.getOrderItems()
                .stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("OrderItem", orderItemId));
    }

    private OrderItem createOrderItem(AddOrderItemRequest request) {

        ProductItemSize size = productItemSizeService.getEntityById(
                request.productItemSizeId());

        return OrderItem.create(
                size.getProductItem(),
                size,
                request.quantity());
    }

    private void recalculateOrder(Order order) {

        BigDecimal itemsTotal = order.getOrderItems()
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(itemsTotal.add(order.getDeliveryPrice()));
    }

    private void validateOrderCanBeEdited(Order order) {

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException(
                    "Order cannot be modified after processing has started.");
        }
    }
}