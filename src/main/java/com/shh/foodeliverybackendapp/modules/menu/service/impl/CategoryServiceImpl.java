package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.CategoryRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.CategoryResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.Category;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.CategoryMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.CategoryRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.shh.foodeliverybackendapp.modules.menu.mapper.CategoryMapper.toEntity;
import static com.shh.foodeliverybackendapp.modules.menu.mapper.CategoryMapper.toResponse;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepo.existsByName(request.name())) {
            throw new EntityAlreadyExistsException(
                    "Category with name '" + request.name() + "' already exists");
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

        category.setName(request.name() !=null ? request.name() : category.getName());
        category.setImageUrl(request.imageUrl() !=null ? request.imageUrl() : category.getImageUrl());
        category.setPosition(request.position() != null ? request.position() : category.getPosition());

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
