package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;

public final class MenuItemSizeMapper {

    private MenuItemSizeMapper() {}

    public static ProductItemSize toEntity(MenuItemSizeRequest request, ProductItem productItem) {
        return new ProductItemSize(
                productItem,
                request.label(),
                request.sizeValue(),
                request.sizeUnit(),
                request.price());
    }

    public static MenuItemSizeResponse toResponse(ProductItemSize size) {
        return new MenuItemSizeResponse(
                size.getId(),
                size.getMenuItem() == null ? null : size.getMenuItem().getId(),
                size.getMenuItem() == null ? null : size.getMenuItem().getName(),
                size.getLabel(),
                size.getSizeValue(),
                size.getSizeUnit(),
                size.getPrice());
    }
}
