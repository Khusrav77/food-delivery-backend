package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.Product;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.ProductRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemService;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepo;
    private final ProductRepository productRepo;
    private final MenuItemTagsService menuItemTagsService;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepo,
                               ProductRepository productRepo,
                               MenuItemTagsService menuItemTagsService) {
        this.menuItemRepo = menuItemRepo;
        this.productRepo = productRepo;
        this.menuItemTagsService = menuItemTagsService;
    }

    @Override
    public MenuItemResponse create(MenuItemRequest request) {
        Product product = productRepo.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product", request.productId()));
        ProductItem productItem = MenuItemMapper.toEntity(request, product);
        return MenuItemMapper.toResponse(menuItemRepo.save(productItem));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse findById(UUID id) {
        return MenuItemMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll() {
        return menuItemRepo.findAll()
                .stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findByProduct(UUID productId) {
        if (!productRepo.existsById(productId)) {
            throw new EntityNotFoundException("Product", productId);
        }
        return menuItemRepo.findByProduct_Id(productId).stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Override
    public MenuItemResponse updateById(UUID id, MenuItemRequest request) {
        ProductItem productItem = getOrThrow(id);

        if (request.productId() != null
                && (productItem.getProduct() == null
                    || !request.productId().equals(productItem.getProduct().getId()))) {
            throw new UnsupportedOperationException(
                    "Re-parenting a menu item to a different product is not supported. "
                            + "Delete and recreate instead.");
        }

        if (request.name() != null) productItem.setName(request.name());
        if (request.active() != null) productItem.setActive(request.active());

        return MenuItemMapper.toResponse(menuItemRepo.save(productItem));
    }

    @Override
    public void deleteById(UUID id) {
        if (!menuItemRepo.existsById(id)) {
            throw new EntityNotFoundException("MenuItem", id);
        }
        menuItemRepo.deleteById(id);
    }

    @Override
    public MenuItemResponse attachTag(UUID menuItemId, UUID tagId) {
        ProductItem productItem = getOrThrow(menuItemId);
        menuItemTagsService.attach(productItem.getId(), tagId);
        return MenuItemMapper.toResponse(getOrThrow(menuItemId));
    }

    @Override
    public MenuItemResponse detachTag(UUID menuItemId, UUID tagId) {
        getOrThrow(menuItemId);
        menuItemTagsService.detach(menuItemId, tagId);
        return MenuItemMapper.toResponse(getOrThrow(menuItemId));
    }

    private ProductItem getOrThrow(UUID id) {
        return menuItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", id));
    }
}
