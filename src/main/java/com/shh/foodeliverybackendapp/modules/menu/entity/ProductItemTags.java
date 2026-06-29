package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;


@Entity
@Table(name = "menu_item_tags")
public class ProductItemTags extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private ProductItem productItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "position", nullable = false)
    private int position = 0;

    protected ProductItemTags() {}

    public ProductItemTags(ProductItem productItem, Tag tag) {
        this.productItem = productItem;
        this.tag = tag;
    }

    public ProductItem getMenuItem() {
        return productItem;
    }
    public void setMenuItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public Tag getTag() {
        return tag;
    }
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}
