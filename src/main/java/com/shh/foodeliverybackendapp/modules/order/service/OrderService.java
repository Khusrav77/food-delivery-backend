package com.shh.foodeliverybackendapp.modules.order.service;

import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderRequest;
import com.shh.foodeliverybackendapp.modules.order.dto.OrderResponse;


public interface OrderService extends CrudAbstractService<OrderRequest, OrderResponse> {
}
