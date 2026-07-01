package com.shh.foodeliverybackendapp.modules.menu.repository;

import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductItemSizeRepository extends JpaRepository<ProductItemSize, UUID> {

    List<ProductItemSize> findByMenuItem_Id(UUID menuItemId);

    boolean existsByMenuItem_IdAndLabel(UUID menuItemId, SizeLabel label);
}
