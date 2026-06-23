package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemImage;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemSize;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemTags;
import com.shh.foodeliverybackendapp.modules.menu.entity.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MenuItemMapper {

    private MenuItemMapper() {}

    public static MenuItem toEntity(MenuItemRequest request, Product product) {
        MenuItem menuItem = new MenuItem(request.name(), product);
        menuItem.setActive(request.active());
        return menuItem;
    }

    public static MenuItemResponse toResponse(MenuItem menuItem) {
        List<String> imageUrls = menuItem.getImages().stream()
                .map(MenuItemImage::getUrl)
                .toList();

        List<MenuItemResponse.SizeView> sizes = menuItem.getSizes().stream()
                .map(MenuItemMapper::toSizeView)
                .toList();

        Set<String> tagLabels = menuItem.getTags().stream()
                .map(MenuItemTags::getTag)
                .filter(tag -> tag != null)
                .map(tag -> tag.getLabel())
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));

        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getProduct() == null ? null : menuItem.getProduct().getId(),
                menuItem.getName(),
                menuItem.getActive(),
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt(),
                imageUrls,
                sizes,
                tagLabels
        );
    }

    private static MenuItemResponse.SizeView toSizeView(MenuItemSize size) {
        return new MenuItemResponse.SizeView(
                size.getId(),
                size.getLabel() == null ? null : size.getLabel().name(),
                size.getSizeValue(),
                size.getSizeUnit() == null ? null : size.getSizeUnit().name(),
                size.getPrice()
        );
    }
}
