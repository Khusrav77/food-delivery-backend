package com.shh.foodeliverybackendapp.modules.menu.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item_sizes")
public class ProductItemSize extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private ProductItem productItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false)
    private SizeLabel label;

    @Column(name = "size_value", nullable = false)
    private BigDecimal sizeValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_unit", nullable = false)
    private SizeUnit sizeUnit;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "position", nullable = false)
    private int position = 0;

    protected ProductItemSize() {}

    public ProductItemSize(ProductItem productItem, SizeLabel label,
                           BigDecimal sizeValue, SizeUnit sizeUnit,
                           BigDecimal price) {
        this.productItem = productItem;
        this.label = label;
        this.sizeValue = sizeValue;
        this.sizeUnit = sizeUnit;
        this.price = price;
    }

    public ProductItem getMenuItem() {
        return productItem;
    }
    public void setMenuItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public SizeLabel getLabel() {
        return label;
    }
    public void setLabel(SizeLabel label) {
        this.label = label;
    }

    public BigDecimal getSizeValue() {
        return sizeValue;
    }
    public void setSizeValue(BigDecimal sizeValue) {
        this.sizeValue = sizeValue;
    }

    public SizeUnit getSizeUnit() {
        return sizeUnit;
    }
    public void setSizeUnit(SizeUnit sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}