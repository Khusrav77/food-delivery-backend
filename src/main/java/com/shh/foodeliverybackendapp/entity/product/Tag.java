package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {

    @Column(name = "label", nullable = false, unique = true)
    private String label;

    protected Tag() {}

    public Tag(String label) {this.label = label;}

    public String getLabel() {return label;}
    public void setLabel(String label) {this.label = label;}

}
