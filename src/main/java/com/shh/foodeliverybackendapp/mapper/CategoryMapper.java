package com.shh.foodeliverybackendapp.mapper;

import com.shh.foodeliverybackendapp.dto.CategoryRequest;
import com.shh.foodeliverybackendapp.dto.CategoryResponse;
import com.shh.foodeliverybackendapp.entity.product.Category;

public final class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category toEntity(CategoryRequest request) {
         Category category = new Category(request.getName());
         category.setPosition(request.getPosition());
         return category;
    }

    public static CategoryResponse toResponse(Category entity) {
        return new  CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getPosition(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
