package com.shh.foodeliverybackendapp.modules.menu.repository;

import com.shh.foodeliverybackendapp.modules.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    List<MenuItem> findByProduct_Id(UUID productId);
}
