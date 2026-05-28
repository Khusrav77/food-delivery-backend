package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.CategoryRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.CategoryResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable UUID id,
                                   @Valid @RequestBody CategoryRequest request) {
        return service.updateById(id, request);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
