package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.selection.Selection;

import java.math.BigDecimal;
import java.util.UUID;

public class AvailableProduct {
    private final UUID sellerId;
    private final UUID productId;
    private final Quantity quantity;
    private final Price price;

    private AvailableProduct(UUID sellerId, UUID productId, Quantity quantity, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static AvailableProduct availableProduct(UUID sellerId, UUID productId, Integer quantity, BigDecimal price) {
        return new AvailableProduct(sellerId, productId, Quantity.quantity(quantity), Price.price(price));
    }

    public boolean isAvailableFor(Selection selection) {
        return selection.hasProductIdSameAs(productId) && selection.hasLessOrEqualThan(quantity);
    }

    public boolean isFor(UUID productId) {
        return this.productId == productId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }
}
