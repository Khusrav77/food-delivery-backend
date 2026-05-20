package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;


@Entity
@Table(name = "menu_item_images")
public class MenuItemImage extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "url", nullable = false)
    private String url;

    protected MenuItemImage() {}

    public MenuItemImage(String url) {
        this.url = url;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
