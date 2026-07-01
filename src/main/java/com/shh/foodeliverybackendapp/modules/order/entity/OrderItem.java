package com.shh.foodeliverybackendapp.modules.order.entity;

import com.shh.foodeliverybackendapp.modules.base.AbstractEntity;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItem;
import com.shh.foodeliverybackendapp.modules.menu.entity.ProductItemSize;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeLabel;
import com.shh.foodeliverybackendapp.modules.menu.entity.SizeUnit;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_item_id", nullable = false)
    private UUID productItemId;

    @Column(name = "product_item_size_id", nullable = false)
    private UUID productItemSizeId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_image")
    private String productImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_label", nullable = false)
    private SizeLabel sizeLabel;

    @Column(name = "size_value", nullable =false, precision = 10, scale = 2)
    private BigDecimal sizeValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_unit", nullable = false)
    private SizeUnit sizeUnit;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    protected OrderItem() {}

    private OrderItem(
            UUID productItemId,
            UUID productItemSizeId,
            String productName,
            String productImage,
            SizeLabel sizeLabel,
            BigDecimal sizeValue,
            SizeUnit sizeUnit,
            BigDecimal unitPrice,
            Integer quantity) {

        this.productItemId = productItemId;
        this.productItemSizeId = productItemSizeId;
        this.productName = productName;
        this.productImage = productImage;
        this.sizeLabel = sizeLabel;
        this.sizeValue = sizeValue;
        this.sizeUnit = sizeUnit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem of(
            ProductItem product,
            ProductItemSize size,
            Integer quantity) {

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (size == null) {
            throw new IllegalArgumentException("Product size cannot be null");
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        return new OrderItem(
                product.getId(),
                size.getId(),
                product.getName(),
                product.getPrimaryImage(),
                size.getLabel(),
                size.getSizeValue(),
                size.getSizeUnit(),
                size.getPrice(),
                quantity);
    }

    void setOrder(Order order) {this.order = order;}

    public Order getOrder() {return order;}

    public UUID getProductItemId() {return productItemId;}

    public UUID getProductItemSizeId() {return productItemSizeId;}

    public String getProductName() {return productName;}

    public String getProductImage() {return productImage;}

    public SizeLabel getSizeLabel() {return sizeLabel;}

    public BigDecimal getSizeValue() {return sizeValue;}

    public SizeUnit getSizeUnit() {return sizeUnit;}

    public BigDecimal getUnitPrice() {return unitPrice;}

    public Integer getQuantity() {return quantity;}

    public BigDecimal getSubtotal() {return subtotal;}
}