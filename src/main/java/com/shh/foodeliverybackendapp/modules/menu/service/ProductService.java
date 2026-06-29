package com.shh.foodeliverybackendapp.modules.menu.service;

import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.menu.dto.request.ProductRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService extends CrudAbstractService<ProductRequest, ProductResponse> {

    List<ProductResponse> findByCategory(UUID categoryId);
    ProductResponse findByName(String name);
}
