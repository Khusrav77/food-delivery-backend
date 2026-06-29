package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;


@Entity
@Table(name = "menu_item_images")
public class ProductItemImage extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private ProductItem productItem;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "position", nullable = false)
    private int position = 0;

    protected ProductItemImage() {}

    public ProductItemImage(String url) {
        this.url = url;
    }

    public ProductItem getMenuItem() {
        return productItem;
    }
    public void setMenuItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}
