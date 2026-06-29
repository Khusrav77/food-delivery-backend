package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemImageResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemImage;

public final class MenuItemImageMapper {

    private MenuItemImageMapper() {}

    public static ProductItemImage toEntity(MenuItemImageRequest request) {
        return new ProductItemImage(request.url());
    }

    public static MenuItemImageResponse toResponse(ProductItemImage image) {
        return new MenuItemImageResponse(
                image.getId(),
                image.getMenuItem() == null ? null : image.getMenuItem().getId(),
                image.getUrl(),
                image.getPosition(),
                image.getCreatedAt(),
                image.getUpdatedAt()
        );
    }
}
