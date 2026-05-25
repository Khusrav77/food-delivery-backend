package com.shh.foodeliverybackendapp.repository;

import com.shh.foodeliverybackendapp.entity.product.MenuItemTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemTagsRepository extends JpaRepository<MenuItemTags, UUID> {

    Optional<MenuItemTags> findByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);

    boolean existsByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);

    void deleteByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);
}
