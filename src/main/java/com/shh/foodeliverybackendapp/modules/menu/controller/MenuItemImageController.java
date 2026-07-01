package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemImageResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-item-images")
public class MenuItemImageController {

    private final ProductItemImageService productItemImageService;

    public MenuItemImageController(ProductItemImageService productItemImageService) {
        this.productItemImageService = productItemImageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemImageResponse create(@Valid @RequestBody MenuItemImageRequest request) {
        return productItemImageService.create(request);
    }

    @GetMapping
    public List<MenuItemImageResponse> listByMenuItem(@RequestParam UUID menuItemId) {
        return productItemImageService.findByMenuItem(menuItemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productItemImageService.deleteById(id);
    }
}
