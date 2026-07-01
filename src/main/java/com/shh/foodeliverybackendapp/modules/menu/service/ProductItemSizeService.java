package com.shh.foodeliverybackendapp.modules.menu.service;

import com.shh.foodeliverybackendapp.modules.base.CrudAbstractService;
import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;

import java.util.List;
import java.util.UUID;

public interface ProductItemSizeService extends CrudAbstractService<MenuItemSizeRequest, MenuItemSizeResponse> {

    /** All sizes that belong to a given menu item. */
    List<MenuItemSizeResponse> findByMenuItem(UUID menuItemId);
    ProductItemSize getEntityById(UUID id);
}
