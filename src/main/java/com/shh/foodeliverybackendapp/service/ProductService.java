package com.shh.foodeliverybackendapp.service;

import com.shh.foodeliverybackendapp.dto.ProductRequest;
import com.shh.foodeliverybackendapp.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService extends BasicService<ProductRequest, ProductResponse> {

    List<ProductResponse> findByCategory(UUID categoryId);
}
