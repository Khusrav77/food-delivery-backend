package com.shh.foodeliverybackendapp.repository;

import com.shh.foodeliverybackendapp.entity.product.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    // Tag entity stores its text as `label` (the table column is also `label`)
    boolean existsByLabel(String label);
}
