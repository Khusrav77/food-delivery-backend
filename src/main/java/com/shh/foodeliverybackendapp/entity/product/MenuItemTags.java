package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;


@Entity
@Table(name = "menu_item_tags")
public class MenuItemTags extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    protected MenuItemTags() {}

    public MenuItemTags(MenuItem menuItem, Tag tag) {
        this.menuItem = menuItem;
        this.tag = tag;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
