package com.shh.foodeliverybackendapp.repository;

import com.shh.foodeliverybackendapp.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategory_Id(UUID categoryId);

    boolean existsByCategory_IdAndName(UUID categoryId, String name);
}
