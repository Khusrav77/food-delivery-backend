package com.shh.foodeliverybackendapp.entity.product;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item_sizes")
public class MenuItemSize extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

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

    protected MenuItemSize() {}

    public MenuItemSize(MenuItem menuItem, SizeLabel label,
                        BigDecimal sizeValue, SizeUnit sizeUnit,
                        BigDecimal price) {
        this.menuItem = menuItem;
        this.label = label;
        this.sizeValue = sizeValue;
        this.sizeUnit = sizeUnit;
        this.price = price;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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
}