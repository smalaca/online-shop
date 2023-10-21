package com.smalaca.purchase.domain.quantitativeproduct;

import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantity.Quantity;
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
}
