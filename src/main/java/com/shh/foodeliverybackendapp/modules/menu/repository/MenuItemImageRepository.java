package com.shh.foodeliverybackendapp.modules.menu.repository;

import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, UUID> {

    List<MenuItemImage> findByMenuItem_IdOrderByPositionAsc(UUID menuItemId);

    boolean existsByMenuItem_IdAndUrl(UUID menuItemId, String url);
}
