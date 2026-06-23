package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
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

    @Column(name = "position", nullable = false)
    private int position = 0;

    protected Tag() {}

    public Tag(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {return label;}
    public void setLabel(String label) {this.label = label;}

    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}
