package com.shh.foodeliverybackendapp.modules.menu.service;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemImageResponse;

import java.util.List;
import java.util.UUID;

public interface MenuItemImageService {

    MenuItemImageResponse create(MenuItemImageRequest request);

    List<MenuItemImageResponse> findByMenuItem(UUID menuItemId);

    void deleteById(UUID id);
}
