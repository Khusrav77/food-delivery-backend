package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItem;
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
        MenuItem menuItem = MenuItemMapper.toEntity(request, product);
        return MenuItemMapper.toResponse(menuItemRepo.save(menuItem));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse findById(UUID id) {

        return MenuItemMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll() {
        return menuItemRepo.findAll().stream()
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
        MenuItem menuItem = getOrThrow(id);

        if (request.productId() != null
                && (menuItem.getProduct() == null
                    || !request.productId().equals(menuItem.getProduct().getId()))) {
            // MenuItem doesn't expose setProduct() today, and re-parenting a
            // menu item to another product is a rare op that tends to indicate
            // a modelling problem. Flag it loudly until there is a real use
            // case and a dedicated endpoint with explicit semantics.
            throw new UnsupportedOperationException(
                    "Re-parenting a menu item to a different product is not supported. "
                            + "Delete and recreate instead."
            );
        }

        if (request.name() != null) menuItem.setName(request.name());
        if (request.active() != null) menuItem.setActive(request.active());

        return MenuItemMapper.toResponse(menuItemRepo.save(menuItem));
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
        MenuItem menuItem = getOrThrow(menuItemId);
        menuItemTagsService.attach(menuItemId, tagId);
        // The cached `tags` collection is stale after the join row was inserted
        // from another bean — re-load the aggregate so the response reflects it.
        return MenuItemMapper.toResponse(getOrThrow(menuItemId));
    }

    @Override
    public MenuItemResponse detachTag(UUID menuItemId, UUID tagId) {
        getOrThrow(menuItemId);
        menuItemTagsService.detach(menuItemId, tagId);
        return MenuItemMapper.toResponse(getOrThrow(menuItemId));
    }

    private MenuItem getOrThrow(UUID id) {
        return menuItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", id));
    }
}
