package com.smalaca.purchase.domain.quantitativeproduct;

import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.selection.Selection;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class QuantitativeProduct {
    private final UUID sellerId;
    private final UUID productId;
    private final Quantity quantity;
    private final Price price;

    public QuantitativeProduct(UUID sellerId, UUID productId, Quantity quantity, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isAvailableFor(Selection selection) {
        return selection.hasProductIdSameAs(productId) && selection.hasLessOrEqualThan(quantity);
    }

    public boolean isFor(UUID productId) {
        return this.productId == productId;
    }
}
