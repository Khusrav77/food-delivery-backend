package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemSizeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-item-sizes")
public class MenuItemSizeController {

    private final MenuItemSizeService menuItemSizeService;

    public MenuItemSizeController(MenuItemSizeService menuItemSizeService) {
        this.menuItemSizeService = menuItemSizeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemSizeResponse create(@Valid @RequestBody MenuItemSizeRequest request) {
        return menuItemSizeService.create(request);
    }

    @GetMapping("/{id}")
    public MenuItemSizeResponse getById(@PathVariable UUID id) {
        return menuItemSizeService.findById(id);
    }

    @GetMapping
    public List<MenuItemSizeResponse> list(@RequestParam(required = false) UUID menuItemId) {
        if (menuItemId != null) {
            return menuItemSizeService.findByMenuItem(menuItemId);
        }
        return menuItemSizeService.findAll();
    }

    @PutMapping("/{id}")
    public MenuItemSizeResponse update(@PathVariable UUID id,
                                       @Valid @RequestBody MenuItemSizeRequest request) {
        return menuItemSizeService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        menuItemSizeService.deleteById(id);
    }
}
