package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@Valid @RequestBody MenuItemRequest request) {
        return menuItemService.create(request);
    }

    @GetMapping("/{id}")
    public MenuItemResponse getById(@PathVariable UUID id) {
        return menuItemService.findById(id);
    }

    @GetMapping
    public List<MenuItemResponse> list(@RequestParam(required = false) UUID productId) {
        if (productId != null) {
            return menuItemService.findByProduct(productId);
        }
        return menuItemService.findAll();
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable UUID id,
                                   @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        menuItemService.deleteById(id);
    }

    @PostMapping("/{menuItemId}/tags/{tagId}")
    public MenuItemResponse attachTag(@PathVariable UUID menuItemId,
                                      @PathVariable UUID tagId) {
        return menuItemService.attachTag(menuItemId, tagId);
    }

    @DeleteMapping("/{menuItemId}/tags/{tagId}")
    public MenuItemResponse detachTag(@PathVariable UUID menuItemId,
                                      @PathVariable UUID tagId) {
        return menuItemService.detachTag(menuItemId, tagId);
    }
}
