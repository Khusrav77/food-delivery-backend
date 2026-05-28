package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemSizeRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.MenuItemSizeResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemSize;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemSizeMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemSizeRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.MenuItemSizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemSizeMapper.toResponse;

@Service
@Transactional
public class MenuItemSizeServiceImpl implements MenuItemSizeService {

    private final MenuItemSizeRepository menuItemSizeRepo;
    private final MenuItemRepository menuItemRepo;

    public MenuItemSizeServiceImpl(MenuItemSizeRepository menuItemSizeRepo,
                                   MenuItemRepository menuItemRepo) {
        this.menuItemSizeRepo = menuItemSizeRepo;
        this.menuItemRepo = menuItemRepo;
    }

    @Override
    public MenuItemSizeResponse create(MenuItemSizeRequest request) {
        MenuItem menuItem = menuItemRepo.findById(request.menuItemId())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", request.menuItemId()));

        MenuItemSize size = MenuItemSizeMapper.toEntity(request, menuItem);
        menuItem.addSize(size);

        // menuItem.addSize() sets the bidirectional link; saving the size directly
        // gives us back the generated id without triggering a full parent reload.
        return toResponse(menuItemSizeRepo.save(size));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemSizeResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
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
        MenuItemSize size = getOrThrow(id);

        if (request.label() != null)     size.setLabel(request.label());
        if (request.sizeValue() != null) size.setSizeValue(request.sizeValue());
        if (request.sizeUnit() != null)  size.setSizeUnit(request.sizeUnit());
        if (request.price() != null)     size.setPrice(request.price());

        // Entity is managed — dirty-checking flushes the update automatically.
        return toResponse(size);
    }

    @Override
    public void deleteById(UUID id) {
        MenuItemSize size = getOrThrow(id);
        // Remove via parent to keep the in-memory collection consistent.
        size.getMenuItem().removeSize(size);
        menuItemSizeRepo.delete(size);
    }

    private MenuItemSize getOrThrow(UUID id) {
        return menuItemSizeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItemSize", id));
    }
}
