package com.shh.foodeliverybackendapp.controller;

import com.shh.foodeliverybackendapp.dto.CategoryRequest;
import com.shh.foodeliverybackendapp.dto.CategoryResponse;
import com.shh.foodeliverybackendapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;
    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public CategoryResponse createCategory(@Validated @RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{d}")
    public CategoryResponse updateCategory(@PathVariable UUID id,
                                           @RequestBody CategoryRequest request) {
        return service.updateById(id, request);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
