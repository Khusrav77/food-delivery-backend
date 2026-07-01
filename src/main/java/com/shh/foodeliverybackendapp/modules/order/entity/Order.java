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
    private BigDecimal deliveryPrice = BigDecimal.ZERO;

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

    public static Order create(
            User user,
            UserAddress address,
            PaymentMethod paymentMethod,
            BigDecimal deliveryPrice,
            String comment) {

        Order order = new Order();
        order.user = user;
        order.address = address;
        order.status = OrderStatus.CREATED;
        order.paymentMethod = paymentMethod;
        order.deliveryPrice = deliveryPrice != null ? deliveryPrice : BigDecimal.ZERO;
        order.comment = comment;
        return order;
    }

    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void clearItems() {
        orderItems.forEach(item -> item.setOrder(null));
        orderItems.clear();
    }

    public User getUser() {return user;}
    public UserAddress getAddress() {return address;}
    public OrderStatus getStatus() {return status;}
    public PaymentMethod getPaymentMethod() {return paymentMethod;}
    public BigDecimal getDeliveryPrice() {return deliveryPrice;}
    public BigDecimal getTotalPrice() {return totalPrice;}
    public String getComment() {return comment;}
    public List<OrderItem> getOrderItems() {return List.copyOf(orderItems);}

    public void setAddress(UserAddress address) {this.address = address;}
    public void setStatus(OrderStatus status) {this.status = status;}
    public void setPaymentMethod(PaymentMethod paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setDeliveryPrice(BigDecimal deliveryPrice) {this.deliveryPrice = deliveryPrice;}
    public void setTotalPrice(BigDecimal totalPrice) {this.totalPrice = totalPrice;}
    public void setComment(String comment) {this.comment = comment;}
}