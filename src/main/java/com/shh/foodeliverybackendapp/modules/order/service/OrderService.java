package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;
import com.shh.foodeliverybackendapp.modules.order.entity.Order;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface OrderService extends CrudAbstractService<OrderRequest, OrderResponse> {
}
