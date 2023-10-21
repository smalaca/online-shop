package com.smalaca.purchase.domain.selection;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.quantity.Quantity;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class Selection {
    private final UUID productId;
    private final Quantity quantity;

    private Selection(UUID productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // to do - should be packaget private after removed in OfferItem
    @Factory
    public static Selection selection(UUID productId, Integer quantity) {
        return new Selection(productId, Quantity.quantity(quantity));
    }

    public boolean hasProductIdSameAs(UUID productId) {
        return this.productId.equals(productId);
    }

    public UUID getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public boolean hasLessThan(Quantity quantity) {
        return this.quantity.isLowerThan(quantity);
    }

    public boolean hasLessOrEqualThan(Quantity quantity) {
        return this.quantity.isLowerOrEqualThan(quantity);
    }
}
