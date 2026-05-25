package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "menu_items")
public class MenuItem extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuItemImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuItemSize> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuItemTags> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected MenuItem() {}

    public MenuItem(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public String getName() {return name;}
    public  void setName(String name) {this.name = name;}

    public boolean getActive() {return isActive;}
    public void setActive(Boolean active) {this.isActive = active;}

    public List<MenuItemImage> getImages() {return images;}

    public List<MenuItemSize> getSizes() {return sizes;}

    public List<MenuItemTags> getTags() {return tags;}

    public Product getProduct() {return product;}


    public void addImage(MenuItemImage image) {
        if (!images.contains(image)) {
            images.add(image);
            image.setMenuItem(this);
        }
    }

    public void removeImage(MenuItemImage image) {
        images.remove(image);
        image.setMenuItem(null);
    }

    public void addSize(MenuItemSize size) {
        sizes.add(size);
        size.setMenuItem(this);
    }

    public void removeSize(MenuItemSize size) {
        sizes.remove(size);
        size.setMenuItem(null);
    }

    public void addTag(MenuItemTags tag) {
        tags.add(tag);
        tag.setMenuItem(this);
    }

    public void removeTag(MenuItemTags tag) {
        tags.remove(tag);
        tag.setMenuItem(null);
    }
}
