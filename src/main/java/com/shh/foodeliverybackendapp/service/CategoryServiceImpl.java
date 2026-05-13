package com.shh.foodeliverybackendapp.service;

import com.shh.foodeliverybackendapp.dto.CategoryRequest;
import com.shh.foodeliverybackendapp.dto.CategoryResponse;
import com.shh.foodeliverybackendapp.entity.product.Category;
import com.shh.foodeliverybackendapp.mapper.CategoryMapper;
import com.shh.foodeliverybackendapp.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;


import static com.shh.foodeliverybackendapp.mapper.CategoryMapper.toEntity;
import static com.shh.foodeliverybackendapp.mapper.CategoryMapper.toResponse;

@Service
public final class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepo.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
        }
        Category category = toEntity(request);
        return toResponse(categoryRepo.save(category));
    }

    @Override
    public CategoryResponse findById(UUID id) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return toResponse(category);
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepo.findAll();
        return categories
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateById(UUID id, CategoryRequest request) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(request.getName());
        category.setImageUrl(request.getImageUrl());
        category.setPosition(request.getPosition());
        return toResponse(categoryRepo.save(category));
    }

    @Override
    public void deleteById(UUID id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepo.deleteById(id);
    }
}
