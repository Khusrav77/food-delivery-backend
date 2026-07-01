package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class ProductItem extends AbstractEntity {

    @Column(name = "name", nullable =false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @OneToMany(
            mappedBy = "menuItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<ProductItemImage> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "menuItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<ProductItemSize> sizes = new ArrayList<>();

    @OneToMany(
            mappedBy = "menuItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<ProductItemTags> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected ProductItem() {}

    public ProductItem(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public void addImage(ProductItemImage image) {
        if (image == null) {return;}

        if (!images.contains(image)) {
            images.add(image);
            image.setMenuItem(this);
        }
    }

    public void removeImage(ProductItemImage image) {
        if (image == null) {return;}
        images.remove(image);
        image.setMenuItem(null);
    }

    public void addSize(ProductItemSize size) {
        if (size == null) {return;}
        sizes.add(size);
        size.setMenuItem(this);
    }

    public void removeSize(ProductItemSize size) {
        if (size == null) {return;}
        sizes.remove(size);
        size.setMenuItem(null);
    }

    public void addTag(ProductItemTags tag) {
        if (tag == null) {return;}
        tags.add(tag);
        tag.setMenuItem(this);
    }

    public void removeTag(ProductItemTags tag) {
        if (tag == null) {return;}
        tags.remove(tag);
        tag.setMenuItem(null);
    }

    public String getPrimaryImage() {

        return images.stream()
                .findFirst()
                .map(ProductItemImage::getUrl)
                .orElse(null);
    }

    public ProductItemSize requireSize(UUID sizeId) {

        return sizes.stream()
                .filter(size -> size.getId().equals(sizeId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Size not found: " + sizeId));
    }

    public BigDecimal getMinPrice() {

        return sizes.stream()
                .map(ProductItemSize::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public String getName() {return name;}
    public boolean isActive() {return active;}
    public Product getProduct() {return product;}
    public List<ProductItemImage> getImages() {return List.copyOf(images);}
    public List<ProductItemSize> getSizes() {return List.copyOf(sizes);}
    public List<ProductItemTags> getTags() {return List.copyOf(tags);}

    public void setName(String name) {this.name = name;}
    public void setActive(boolean active) {this.active = active;}
}