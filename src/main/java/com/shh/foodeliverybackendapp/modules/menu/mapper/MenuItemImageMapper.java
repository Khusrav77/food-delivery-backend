package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemImageResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemImage;

public final class MenuItemImageMapper {

    private MenuItemImageMapper() {}

    public static MenuItemImage toEntity(MenuItemImageRequest request) {
        return new MenuItemImage(request.url());
    }

    public static MenuItemImageResponse toResponse(MenuItemImage image) {
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
