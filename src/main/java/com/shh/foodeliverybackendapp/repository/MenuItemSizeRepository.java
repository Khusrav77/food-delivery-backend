package com.shh.foodeliverybackendapp.repository;

import com.shh.foodeliverybackendapp.entity.product.MenuItemSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemSizeRepository extends JpaRepository<MenuItemSize, UUID> {

    List<MenuItemSize> findByMenuItem_Id(UUID menuItemId);

    boolean existsByMenuItem_IdAndLabel(UUID menuItemId, com.shh.foodeliverybackendapp.entity.product.SizeLabel label);
}
