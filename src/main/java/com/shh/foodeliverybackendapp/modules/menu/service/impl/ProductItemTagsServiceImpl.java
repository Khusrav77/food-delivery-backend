package com.shh.foodeliverybackendapp.modules.menu.service.impl;

import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemTags;
import com.shh.foodeliverybackendapp.modules.menu.entity.Tag;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.modules.menu.repository.ProductItemRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.ProductItemTagsRepository;
import com.shh.foodeliverybackendapp.modules.menu.repository.TagRepository;
import com.shh.foodeliverybackendapp.modules.menu.service.ProductItemTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProductItemTagsServiceImpl implements ProductItemTagsService {

    private final ProductItemTagsRepository menuItemTagsRepo;
    private final ProductItemRepository menuItemRepo;
    private final TagRepository tagRepo;

    public ProductItemTagsServiceImpl(ProductItemTagsRepository menuItemTagsRepo,
                                      ProductItemRepository menuItemRepo,
                                      TagRepository tagRepo) {
        this.menuItemTagsRepo = menuItemTagsRepo;
        this.menuItemRepo = menuItemRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public void attach(UUID menuItemId, UUID tagId) {
        if (menuItemTagsRepo.existsByMenuItem_IdAndTag_Id(menuItemId, tagId)) {
            return; // idempotent — nothing to do
        }
        ProductItem productItem = menuItemRepo.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemId));
        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag", tagId));

        ProductItemTags link = new ProductItemTags(productItem, tag);
        menuItemTagsRepo.save(link);
    }

    @Override
    public void detach(UUID menuItemId, UUID tagId) {
        // Idempotent: deleteBy... is a no-op if nothing matches.
        menuItemTagsRepo.deleteByMenuItem_IdAndTag_Id(menuItemId, tagId);
    }
}
