package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.dto.request.MenuItemImageRequest;
import com.shh.foodeliverybackendapp.modules.menu.dto.response.MenuItemImageResponse;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemImage;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemImageMapper;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemImageRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.MenuItemRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.shh.foodeliverybackendapp.modules.menu.mapper.MenuItemImageMapper.toResponse;

@Service
@Transactional
public class ProductItemImageServiceImpl implements ProductItemImageService {

    private final MenuItemImageRepository menuItemImageRepo;
    private final MenuItemRepository menuItemRepo;

    public ProductItemImageServiceImpl(MenuItemImageRepository menuItemImageRepo,
                                       MenuItemRepository menuItemRepo) {
        this.menuItemImageRepo = menuItemImageRepo;
        this.menuItemRepo = menuItemRepo;
    }

    @Override
    public MenuItemImageResponse create(MenuItemImageRequest request) {
        ProductItem productItem = menuItemRepo.findById(request.menuItemId())
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", request.menuItemId()));

        if (menuItemImageRepo.existsByMenuItem_IdAndUrl(request.menuItemId(), request.url())) {
            throw new EntityAlreadyExistsException(
                    "Image with url '" + request.url() + "' already exists for this menu item");
        }

        ProductItemImage image = MenuItemImageMapper.toEntity(request);
        productItem.addImage(image);

        return toResponse(menuItemImageRepo.save(image));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemImageResponse> findByMenuItem(UUID menuItemId) {
        if (!menuItemRepo.existsById(menuItemId)) {
            throw new EntityNotFoundException("MenuItem", menuItemId);
        }
        return menuItemImageRepo.findByMenuItem_IdOrderByPositionAsc(menuItemId)
                .stream()
                .map(MenuItemImageMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        ProductItemImage image = menuItemImageRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItemImage", id));
        image.getMenuItem().removeImage(image);
        menuItemImageRepo.delete(image);
    }
}
