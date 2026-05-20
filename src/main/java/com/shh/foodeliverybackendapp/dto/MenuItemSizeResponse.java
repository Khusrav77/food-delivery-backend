package com.shh.foodeliverybackendapp.dto;


import com.shh.foodeliverybackendapp.entity.product.SizeLabel;
import com.shh.foodeliverybackendapp.entity.product.SizeUnit;

import java.math.BigDecimal;

public class MenuItemSizeResponse {

    private String id;
    private String menuItemId;
    private String menuItemName;

    private SizeLabel label;

    private BigDecimal sizeValue;

    private SizeUnit sizeUnit;

    private BigDecimal price;

    public MenuItemSizeResponse() {}

    public MenuItemSizeResponse(String id,
                                String menuItemId,
                                String menuItemName,
                                SizeLabel label,
                                BigDecimal sizeValue,
                                SizeUnit sizeUnit,
                                BigDecimal price) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.label = label;
        this.sizeValue = sizeValue;
        this.sizeUnit = sizeUnit;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
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
