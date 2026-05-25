package com.shh.foodeliverybackendapp.service.impl;

import com.shh.foodeliverybackendapp.entity.product.MenuItem;
import com.shh.foodeliverybackendapp.entity.product.MenuItemTags;
import com.shh.foodeliverybackendapp.entity.product.Tag;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.repository.MenuItemRepository;
import com.shh.foodeliverybackendapp.repository.MenuItemTagsRepository;
import com.shh.foodeliverybackendapp.repository.TagRepository;
import com.shh.foodeliverybackendapp.service.MenuItemTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MenuItemTagsServiceImpl implements MenuItemTagsService {

    private final MenuItemTagsRepository menuItemTagsRepo;
    private final MenuItemRepository menuItemRepo;
    private final TagRepository tagRepo;

    public MenuItemTagsServiceImpl(MenuItemTagsRepository menuItemTagsRepo,
                                   MenuItemRepository menuItemRepo,
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
        MenuItem menuItem = menuItemRepo.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem", menuItemId));
        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag", tagId));

        MenuItemTags link = new MenuItemTags(menuItem, tag);
        menuItemTagsRepo.save(link);
    }

    @Override
    public void detach(UUID menuItemId, UUID tagId) {
        // Idempotent: deleteBy... is a no-op if nothing matches.
        menuItemTagsRepo.deleteByMenuItem_IdAndTag_Id(menuItemId, tagId);
    }
}
