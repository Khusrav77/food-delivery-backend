package com.shh.foodeliverybackendapp.dto;


import com.shh.foodeliverybackendapp.entity.product.SizeLabel;
import com.shh.foodeliverybackendapp.entity.product.SizeUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MenuItemSizeRequest {

    @NotNull
    private String menuItemId;

    @NotNull
    private SizeLabel label;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal sizeValue;

    @NotNull
    private SizeUnit sizeUnit;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    protected MenuItemSizeRequest() {}

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
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