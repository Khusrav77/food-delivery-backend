package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController {

    private final ProductItemService productItemService;

    public MenuItemController(ProductItemService productItemService) {
        this.productItemService = productItemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@Valid @RequestBody MenuItemRequest request) {
        return productItemService.create(request);
    }

    @GetMapping("/{id}")
    public MenuItemResponse getById(@PathVariable UUID id) {
        return productItemService.findById(id);
    }

    @GetMapping
    public List<MenuItemResponse> list(@RequestParam(required = false) UUID productId) {
        if (productId != null) {
            return productItemService.findByProduct(productId);
        }
        return productItemService.findAll();
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable UUID id,
                                   @Valid @RequestBody MenuItemRequest request) {
        return productItemService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productItemService.deleteById(id);
    }

    @PostMapping("/{menuItemId}/tags/{tagId}")
    public MenuItemResponse attachTag(@PathVariable UUID menuItemId,
                                      @PathVariable UUID tagId) {
        return productItemService.attachTag(menuItemId, tagId);
    }

    @DeleteMapping("/{menuItemId}/tags/{tagId}")
    public MenuItemResponse detachTag(@PathVariable UUID menuItemId,
                                      @PathVariable UUID tagId) {
        return productItemService.detachTag(menuItemId, tagId);
    }
}
