package com.shh.foodeliverybackendapp.modules.menu.mapper;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemImage;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemTags;
import com.shh.foodeliverybackendapp.modules.menu.entity.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MenuItemMapper {

    private MenuItemMapper() {}

    public static ProductItem toEntity(MenuItemRequest request, Product product) {
        ProductItem productItem = new ProductItem(request.name(), product);
        productItem.setActive(request.active());
        return productItem;
    }

    public static MenuItemResponse toResponse(ProductItem productItem) {
        List<String> imageUrls = productItem.getImages().stream()
                .map(ProductItemImage::getUrl)
                .toList();

        List<MenuItemResponse.SizeView> sizes = productItem.getSizes().stream()
                .map(MenuItemMapper::toSizeView)
                .toList();

        Set<String> tagLabels = productItem.getTags().stream()
                .map(ProductItemTags::getTag)
                .filter(tag -> tag != null)
                .map(tag -> tag.getLabel())
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));

        return new MenuItemResponse(
                productItem.getId(),
                productItem.getProduct() == null ? null : productItem.getProduct().getId(),
                productItem.getName(),
                productItem.getActive(),
                productItem.getCreatedAt(),
                productItem.getUpdatedAt(),
                imageUrls,
                sizes,
                tagLabels);
    }

    private static MenuItemResponse.SizeView toSizeView(ProductItemSize size) {
        return new MenuItemResponse.SizeView(
                size.getId(),
                size.getLabel() == null ? null : size.getLabel().name(),
                size.getSizeValue(),
                size.getSizeUnit() == null ? null : size.getSizeUnit().name(),
                size.getPrice());
    }
}
