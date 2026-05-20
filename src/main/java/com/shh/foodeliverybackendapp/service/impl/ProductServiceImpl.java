package com.shh.foodeliverybackendapp.service.impl;

import com.shh.foodeliverybackendapp.dto.ProductRequest;
import com.shh.foodeliverybackendapp.dto.ProductResponse;
import com.shh.foodeliverybackendapp.entity.product.Category;
import com.shh.foodeliverybackendapp.entity.product.Product;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.mapper.ProductMapper;
import com.shh.foodeliverybackendapp.repository.CategoryRepository;
import com.shh.foodeliverybackendapp.repository.ProductRepository;
import com.shh.foodeliverybackendapp.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(ProductRepository productRepo,
                              CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category", request.getCategoryId()));

        if (productRepo.existsByCategory_IdAndName(category.getId(), request.getName())) {
            throw new EntityAlreadyExistsException(
                    "Product with name '" + request.getName() + "' already exists in this category");
        }

        Product product = ProductMapper.toEntity(request, category);
        return ProductMapper.toResponse(productRepo.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepo.findAll().stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(UUID categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new EntityNotFoundException("Category", categoryId);
        }
        return productRepo.findByCategory_Id(categoryId).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateById(UUID id, ProductRequest request) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));

        if (request.getCategoryId() != null
                && (product.getCategoryId() == null
                    || !request.getCategoryId().equals(product.getCategoryId().getId()))) {
            Category newCategory = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category", request.getCategoryId()));
            product.setCategory(newCategory);
        }

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getActive() != null) product.setActive(request.getActive());
        if (request.getPosition() != null) product.setPosition(request.getPosition());

        return ProductMapper.toResponse(productRepo.save(product));
    }

    @Override
    public void deleteById(UUID id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product", id);
        }
        productRepo.deleteById(id);
    }
}
