package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemSizeMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.ProductItemSizeRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemSizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemSizeMapper.toResponse;

@Service
@Transactional
public class ProductItemSizeServiceImpl implements ProductItemSizeService {

    private final ProductItemSizeRepository menuItemSizeRepo;
    private final MenuItemRepository menuItemRepo;

    public ProductItemSizeServiceImpl(ProductItemSizeRepository menuItemSizeRepo,
                                      MenuItemRepository menuItemRepo) {
        this.menuItemSizeRepo = menuItemSizeRepo;
        this.menuItemRepo = menuItemRepo;
    }

    @Override
    public MenuItemSizeResponse create(MenuItemSizeRequest request) {
        ProductItem productItem = menuItemRepo.findById(request.menuItemId())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", request.menuItemId()));

        ProductItemSize size = MenuItemSizeMapper.toEntity(request, productItem);
        productItem.addSize(size);

        return toResponse(menuItemSizeRepo.save(size));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemSizeResponse findById(UUID id) {
        return toResponse(getEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemSizeResponse> findAll() {
        return menuItemSizeRepo.findAll().stream()
                .map(MenuItemSizeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemSizeResponse> findByMenuItem(UUID menuItemId) {
        if (!menuItemRepo.existsById(menuItemId)) {
            throw new EntityNotFoundException("MenuItem", menuItemId);
        }
        return menuItemSizeRepo.findByMenuItem_Id(menuItemId).stream()
                .map(MenuItemSizeMapper::toResponse)
                .toList();
    }

    @Override
    public MenuItemSizeResponse updateById(UUID id, MenuItemSizeRequest request) {
        ProductItemSize size = getEntityById(id);

        if (request.label() != null)     size.setLabel(request.label());
        if (request.sizeValue() != null) size.setSizeValue(request.sizeValue());
        if (request.sizeUnit() != null)  size.setSizeUnit(request.sizeUnit());
        if (request.price() != null)     size.setPrice(request.price());

        return toResponse(size);
    }

    @Override
    public void deleteById(UUID id) {
        ProductItemSize size = getEntityById(id);
        size.getMenuItem().removeSize(size);
        menuItemSizeRepo.delete(size);
    }

    @Override
    public ProductItemSize getEntityById(UUID id) {
        return menuItemSizeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItemSize", id));
    }

}
