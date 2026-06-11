package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemSize;

public final class MenuItemSizeMapper {

    private MenuItemSizeMapper() {}

    public static MenuItemSize toEntity(MenuItemSizeRequest request, MenuItem menuItem) {
        return new MenuItemSize(
                menuItem,
                request.label(),
                request.sizeValue(),
                request.sizeUnit(),
                request.price()
        );
    }

    public static MenuItemSizeResponse toResponse(MenuItemSize size) {
        return new MenuItemSizeResponse(
                size.getId(),
                size.getMenuItem() == null ? null : size.getMenuItem().getId(),
                size.getMenuItem() == null ? null : size.getMenuItem().getName(),
                size.getLabel(),
                size.getSizeValue(),
                size.getSizeUnit(),
                size.getPrice()
        );
    }
}
