package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemSizeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-item-sizes")
public class MenuItemSizeController {

    private final ProductItemSizeService productItemSizeService;

    public MenuItemSizeController(ProductItemSizeService productItemSizeService) {
        this.productItemSizeService = productItemSizeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemSizeResponse create(@Valid @RequestBody MenuItemSizeRequest request) {
        return productItemSizeService.create(request);
    }

    @GetMapping("/{id}")
    public MenuItemSizeResponse getById(@PathVariable UUID id) {
        return productItemSizeService.findById(id);
    }

    @GetMapping
    public List<MenuItemSizeResponse> list(@RequestParam(required = false) UUID menuItemId) {
        if (menuItemId != null) {
            return productItemSizeService.findByMenuItem(menuItemId);
        }
        return productItemSizeService.findAll();
    }

    @PutMapping("/{id}")
    public MenuItemSizeResponse update(@PathVariable UUID id,
                                       @Valid @RequestBody MenuItemSizeRequest request) {
        return productItemSizeService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productItemSizeService.deleteById(id);
    }
}
