package com.shh.foodeliverybackendapp.modules.menu.controller;

import com.shh.foodeliverybackendapp.modules.menu.dto.ProductRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.ProductResponse;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<ProductResponse> list(@RequestParam(required = false) UUID categoryId) {
        if (categoryId != null) {
            return productService.findByCategory(categoryId);
        }
        return productService.findAll();
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id,
                                  @Valid @RequestBody ProductRequest request) {
        return productService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.deleteById(id);
    }
}
