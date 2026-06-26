package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.ProductRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.ProductResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.Category;
import com.shh.foodeliverybackendapp.modules.menu.entity.Product;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.ProductMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.CategoryRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.ProductRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductService;
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
        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category", request.categoryId()));

        if (productRepo.existsByCategory_IdAndName(category.getId(), request.name())) {
            throw new EntityAlreadyExistsException(
                    "Product with name '" + request.name() + "' already exists in this category");
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
    public ProductResponse findByName(String name) {
       Product product = productRepo.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product", name));
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

        if (request.categoryId() != null
                && (product.getCategoryId() == null
                    || !request.categoryId().equals(product.getCategoryId().getId()))) {
            Category newCategory = categoryRepo.findById(request.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category", request.categoryId()));
            product.setCategory(newCategory);
        }

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.active() != null) product.setIsActive(request.active());
        if (request.position() != null) product.setPosition(request.position());

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
