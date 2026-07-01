package com.shh.foodeliverybackendapp.modules.order.repository;

import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndUser(UUID id, User currentUser);
    List<Order> findAllByUser(User currentUser);
}
