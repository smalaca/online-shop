package com.smalaca.purchase.domain.product;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionFactory;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class Product {
    private final UUID productId;
    private final Quantity quantity;

    private Product(UUID productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Factory
    public static Product product(UUID productId, Integer quantity) {
        return new Product(productId, Quantity.quantity(quantity));
    }

    public boolean hasProductIdSameAs(UUID productId) {
        return this.productId.equals(productId);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public boolean hasLessThan(Quantity quantity) {
        return this.quantity.isLowerThan(quantity);
    }

    public boolean hasLessOrEqualThan(Quantity quantity) {
        return this.quantity.isLowerOrEqualThan(quantity);
    }

    public Selection asSelection() {
        return SelectionFactory.selection(getProductId(), getQuantity().getValue());
    }
}
