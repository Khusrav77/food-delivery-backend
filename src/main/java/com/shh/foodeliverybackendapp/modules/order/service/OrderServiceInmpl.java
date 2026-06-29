package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.repository.OrderRepository;
import com.shh.foodeliverybackendapp.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceInmpl implements OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public OrderServiceInmpl(UserRepository userRepo, OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        return null;
    }

    @Override
    public OrderResponse findById(UUID id) {
        return null;
    }

    @Override
    public List<OrderResponse> findAll() {
        return List.of();
    }

    @Override
    public OrderResponse updateById(UUID id, OrderRequest request) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
