package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "position", nullable = false)
    private int position = 0;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    protected Category() {}

    public Category(String name) {this.name = name;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}