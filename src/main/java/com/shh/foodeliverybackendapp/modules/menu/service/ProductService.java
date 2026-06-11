package com.shh.foodeliverybackendapp.modules.menu.service;

import com.shh.foodeliverybackendapp.modules.base.BasicService;
import com.shh.foodeliverybackendapp.modules.menu.dto.request.ProductRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService extends BasicService<ProductRequest, ProductResponse> {

    List<ProductResponse> findByCategory(UUID categoryId);
    ProductResponse findByName(String name);
}
