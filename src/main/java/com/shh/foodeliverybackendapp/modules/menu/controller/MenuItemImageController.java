package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemImageResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-item-images")
public class MenuItemImageController {

    private final MenuItemImageService menuItemImageService;

    public MenuItemImageController(MenuItemImageService menuItemImageService) {
        this.menuItemImageService = menuItemImageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemImageResponse create(@Valid @RequestBody MenuItemImageRequest request) {
        return menuItemImageService.create(request);
    }

    @GetMapping
    public List<MenuItemImageResponse> listByMenuItem(@RequestParam UUID menuItemId) {
        return menuItemImageService.findByMenuItem(menuItemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        menuItemImageService.deleteById(id);
    }
}
