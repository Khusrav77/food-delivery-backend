package com.shh.foodeliverybackendapp.repository;

import com.shh.foodeliverybackendapp.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
