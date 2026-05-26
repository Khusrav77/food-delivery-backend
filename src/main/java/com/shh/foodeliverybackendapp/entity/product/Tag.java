package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {

    @Column(name = "label", nullable = false, unique = true)
    private String label;

    @Column(name = "color", nullable = false, unique = true)
    private  String color;

    protected Tag() {}

    public Tag(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {return label;}
    public String getColor() {return color;}
    public void setLabel(String label) {this.label = label;}

}
