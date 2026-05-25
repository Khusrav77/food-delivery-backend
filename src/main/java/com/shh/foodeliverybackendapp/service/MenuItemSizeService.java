package com.shh.foodeliverybackendapp.service;

import com.shh.foodeliverybackendapp.dto.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.dto.MenuItemSizeResponse;

import java.util.List;
import java.util.UUID;

public interface MenuItemSizeService extends BasicService<MenuItemSizeRequest, MenuItemSizeResponse> {

    /** All sizes that belong to a given menu item. */
    List<MenuItemSizeResponse> findByMenuItem(UUID menuItemId);
}
