package com.shh.foodeliverybackendapp.service.impl;

import com.shh.foodeliverybackendapp.dto.CategoryRequest;
import com.shh.foodeliverybackendapp.dto.CategoryResponse;
import com.shh.foodeliverybackendapp.entity.product.Category;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.mapper.CategoryMapper;
import com.shh.foodeliverybackendapp.repository.CategoryRepository;
import com.shh.foodeliverybackendapp.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.shh.foodeliverybackendapp.mapper.CategoryMapper.toEntity;
import static com.shh.foodeliverybackendapp.mapper.CategoryMapper.toResponse;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepo.existsByName(request.getName())) {
            throw new EntityAlreadyExistsException(
                    "Category with name '" + request.getName() + "' already exists");
        }
        Category category = toEntity(request);
        return toResponse(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(UUID id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category", id));
        return toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepo.findAll().stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateById(UUID id, CategoryRequest request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category", id));
        if (request.getName() != null) category.setName(request.getName());
        if (request.getImageUrl() != null) category.setImageUrl(request.getImageUrl());
        if (request.getPosition() != null) category.setPosition(request.getPosition());
        return toResponse(categoryRepo.save(category));
    }

    @Override
    public void deleteById(UUID id) {
        if (!categoryRepo.existsById(id)) {
            throw new EntityNotFoundException("Category", id);
        }
        categoryRepo.deleteById(id);
    }
}
