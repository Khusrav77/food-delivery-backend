package com.shh.foodeliverybackendapp.modules.menu.repository;

import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItemSize;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemSizeRepository extends JpaRepository<MenuItemSize, UUID> {

    List<MenuItemSize> findByMenuItem_Id(UUID menuItemId);

    boolean existsByMenuItem_IdAndLabel(UUID menuItemId, SizeLabel label);
}
