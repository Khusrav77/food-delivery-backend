package com.shh.foodeliverybackendapp.modules.order.entity;

import com.shh.foodeliverybackendapp.modules.address.entity.UserAddress;
import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private UserAddress address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "delivery_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryPrice;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<OrderItem> orderItems = new ArrayList<>();

    protected Order() {}

    public Order(User user,
                 UserAddress address,
                 OrderStatus status,
                 PaymentMethod paymentMethod,
                 BigDecimal deliveryPrice,
                 String comment) {

        this.user = user;
        this.address = address;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryPrice = deliveryPrice != null
                ? deliveryPrice
                : BigDecimal.ZERO;
        this.comment = comment;
    }

    public void addItem(OrderItem item) {

        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }

        orderItems.add(item);
        item.setOrder(this);
        calculateTotalPrice();
    }

    public void removeItem(OrderItem item) {

        if (item == null) {return;}

        orderItems.remove(item);
        item.setOrder(null);
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {

        BigDecimal itemsTotal = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalPrice = itemsTotal.add(deliveryPrice);
    }

    public void changeStatus(OrderStatus status) {this.status = status;}

    public void changeAddress(UserAddress address) {this.address = address;}

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void changeComment(String comment) {this.comment = comment;}

    public void changeDeliveryPrice(BigDecimal deliveryPrice) {

        this.deliveryPrice = deliveryPrice != null ? deliveryPrice : BigDecimal.ZERO;
        calculateTotalPrice();
    }


    public User getUser() {return user;}

    public UserAddress getAddress() {return address;}

    public OrderStatus getStatus() {return status;}

    public PaymentMethod getPaymentMethod() {return paymentMethod;}

    public BigDecimal getDeliveryPrice() {return deliveryPrice;}

    public BigDecimal getTotalPrice() {return totalPrice;}

    public String getComment() {return comment;}

    public List<OrderItem> getOrderItems() {return List.copyOf(orderItems);}
}