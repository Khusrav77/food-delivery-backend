package com.shh.foodeliverybackendapp.service;

import com.shh.foodeliverybackendapp.dto.MenuItemRequest;
import com.shh.foodeliverybackendapp.dto.MenuItemResponse;

import java.util.List;
import java.util.UUID;

public interface MenuItemService extends BasicService<MenuItemRequest, MenuItemResponse> {

    /** Menu items that belong to a given product. */
    List<MenuItemResponse> findByProduct(UUID productId);

    /** Attach an existing tag to a menu item. No-op if already attached. */
    MenuItemResponse attachTag(UUID menuItemId, UUID tagId);

    /** Remove the given tag from a menu item. No-op if it was not attached. */
    MenuItemResponse detachTag(UUID menuItemId, UUID tagId);
}
