package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "menu_items")
public class ProductItem extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductItemImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductItemSize> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductItemTags> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected ProductItem() {}

    public ProductItem(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public String getName() {return name;}
    public  void setName(String name) {this.name = name;}

    public boolean getActive() {return isActive;}
    public void setActive(Boolean active) {this.isActive = active;}

    public List<ProductItemImage> getImages() {return images;}

    public List<ProductItemSize> getSizes() {return sizes;}

    public List<ProductItemTags> getTags() {return tags;}

    public Product getProduct() {return product;}


    public void addImage(ProductItemImage image) {
        if (!images.contains(image)) {
            images.add(image);
            image.setMenuItem(this);
        }
    }

    public void removeImage(ProductItemImage image) {
        images.remove(image);
        image.setMenuItem(null);
    }

    public void addSize(ProductItemSize size) {
        sizes.add(size);
        size.setMenuItem(this);
    }

    public void removeSize(ProductItemSize size) {
        sizes.remove(size);
        size.setMenuItem(null);
    }

    public void addTag(ProductItemTags tag) {
        tags.add(tag);
        tag.setMenuItem(this);
    }

    public void removeTag(ProductItemTags tag) {
        tags.remove(tag);
        tag.setMenuItem(null);
    }
}
