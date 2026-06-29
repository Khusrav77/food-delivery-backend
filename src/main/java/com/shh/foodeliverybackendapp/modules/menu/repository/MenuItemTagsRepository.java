package com.shh.foodeliverybackendapp.modules.menu.repository;

import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemTagsRepository extends JpaRepository<ProductItemTags, UUID> {

    Optional<ProductItemTags> findByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);

    boolean existsByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);

    void deleteByMenuItem_IdAndTag_Id(UUID menuItemId, UUID tagId);
}
