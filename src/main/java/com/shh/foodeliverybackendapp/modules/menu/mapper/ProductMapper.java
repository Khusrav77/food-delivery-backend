package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.ProductRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.ProductResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.Category;
import com.shh.foodeliverybackendapp.modules.menu.entity.Product;

public final class ProductMapper {

    private ProductMapper() {}

    public static Product toEntity(ProductRequest request, Category category) {

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);
        product.setIsActive(request.active());

        product.setPosition(
                request.position() != null ? request.position() : 0
        );

        return product;
    }

    public static ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getCategoryId() == null ? null : product.getCategoryId().getId(),
                product.getName(),
                product.getDescription(),
                product.getIsActive(),
                product.getPosition(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}