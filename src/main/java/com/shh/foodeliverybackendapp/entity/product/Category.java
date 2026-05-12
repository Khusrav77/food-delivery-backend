package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public final class Category extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;


    protected Category() {}
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}