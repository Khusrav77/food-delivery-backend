package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.address.entity.UserAddress;
import com.shh.foodeliverybackendapp.modules.address.service.UserAddressServiceImpl;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemSizeService;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderItemRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.order.entity.OrderItem;
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
                request.comment()
        );

        List<OrderItem> items = request.items()
                .stream()
                .map(this::createOrderItem)
                .toList();

        BigDecimal itemsTotal = items
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        items.forEach(order::addItem);
        order.setTotalPrice(itemsTotal.add(order.getDeliveryPrice()));
        Order saved = orderRepo.save(order);
        return orderMapper.toResponse(saved);
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
    public OrderResponse updateById(UUID id, OrderRequest request) {
        throw new UnsupportedOperationException("Updating orders is not supported");
    }

    @Override
    public void deleteById(UUID id) {
        Order order = getOrderById(id);
        orderRepo.delete(order);
    }

    private Order getOrderById(UUID id) {
        User currentUser = userService.getCurrentUserEntity();
        return orderRepo.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));
    }

    private OrderItem createOrderItem(OrderItemRequest request) {
        ProductItemSize size = productItemSizeService.getEntityById(
                request.productItemSizeId());

        return OrderItem.of(size.getMenuItem(), size, request.quantity());
    }
}